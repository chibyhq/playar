package client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.chiby.player.PygamezeroExecutor;
import com.github.chiby.player.model.Application;
import com.github.chiby.player.model.LogEntry;
import com.github.chiby.player.model.RunSession;
import com.github.chiby.store.model.repositories.LogEntryRepository;
import com.github.chiby.store.model.repositories.RunSessionRepository;

@RunWith(MockitoJUnitRunner.class)
public class PygamezeroExecutorTest {
	@Mock
	LogEntryRepository logEntryRepository;

	@Mock
	RunSessionRepository runSessionRepository;

	/**
	 * Test that the standard output and standard error streams are properly
	 * handled in a multithreaded manner by executor.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLogFlushing() throws Exception {
		Application app = Application.builder().uuid(UUID.randomUUID()).environment(new HashMap<>()).build();
		PygamezeroExecutor pgze = new PygamezeroExecutor();
		pgze.setLogEntryRepository(logEntryRepository);
		pgze.setRunSessionRepository(runSessionRepository);

		Path tempDirectory = Files.createTempDirectory("chiby-pgz-test");
		Path appPyFile = Files.createFile(tempDirectory.resolve("application.py"));
		Files.write(appPyFile,
				String.join("\n", "import time", "WIDTH=10", "HEIGHT=10", "def draw():", "  screen.fill((128,0,0))",
						"print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)",
						"time.sleep(1)", "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)", "time.sleep(1)",
						"quit()").getBytes());

		RunSession session = new RunSession();
		session.setApplication(app);
		pgze.start(app, session, tempDirectory);

		// Thread.sleep(500);
		// verify(logEntryRepository, times(3)).save((LogEntry)any());
		Thread.sleep(3000);

		// In total, we should have saved five lines of output
		verify(logEntryRepository, times(5)).save((LogEntry) any());
	}

	@Test
	public void testProcessStop() throws Exception {
		Application app = Application.builder().uuid(UUID.randomUUID()).environment(new HashMap<>()).build();
		PygamezeroExecutor pgze = new PygamezeroExecutor();
		pgze.setLogEntryRepository(logEntryRepository);
		pgze.setRunSessionRepository(runSessionRepository);

		Path tempDirectory = Files.createTempDirectory("chiby-pgz-test");
		Path appPyFile = Files.createFile(tempDirectory.resolve("application.py"));
		Files.write(appPyFile,
				String.join("\n", "import time", "WIDTH=10", "HEIGHT=10", "def draw():", "  screen.fill((128,0,0))",
						"print('STARTED',flush=True)", "time.sleep(20)", "print('FINISHED',flush=True)",
						"quit()").getBytes());

		RunSession session = new RunSession();
		session.setApplication(app);

		pgze.start(app, session, tempDirectory);

		assertTrue(session.getExecutionId() != null);
		assertTrue(pgze.getProcessForExecutionId(session.getExecutionId()).isAlive());
		
		Thread.sleep(1000);

		pgze.stop(session, false);
		Thread.sleep(1000);
		assertFalse(pgze.getProcessForExecutionId(session.getExecutionId()).isAlive());
		verify(runSessionRepository, atLeastOnce()).save(session);
	}
	
	@Test
	public void testProcessWatcher() throws Exception {
		Application app = Application.builder().uuid(UUID.randomUUID()).environment(new HashMap<>()).build();
		PygamezeroExecutor pgze = new PygamezeroExecutor();
		pgze.setLogEntryRepository(logEntryRepository);
		pgze.setRunSessionRepository(runSessionRepository);

		Path tempDirectory = Files.createTempDirectory("chiby-pgz-test");
		Path appPyFile = Files.createFile(tempDirectory.resolve("application.py"));
		Files.write(appPyFile,
				String.join("\n", "import time","import sys", "WIDTH=10", "HEIGHT=10", "def draw():", "  screen.fill((128,0,0))",
						"print('STARTED',flush=True)", "time.sleep(1)", "print('FINISHED',flush=True)",
						"sys.exit(1)").getBytes());

		RunSession session = new RunSession();
		session.setApplication(app);
		
		when(runSessionRepository.findOneByExecutionIdAndRunning(any(), eq(true))).thenReturn(session);
		
		ArgumentCaptor<RunSession> runSessionCaptor = ArgumentCaptor.forClass(RunSession.class);

		pgze.start(app, session, tempDirectory);

		assertTrue(session.getExecutionId() != null);
		assertTrue(pgze.getProcessForExecutionId(session.getExecutionId()).isAlive());
		
		Thread.sleep(3000);

		// At this stage, the runSession should have been marked as stopped
		verify(runSessionRepository, times(2)).save(runSessionCaptor.capture());
		
		List<RunSession> captured = runSessionCaptor.getAllValues();
		assertEquals(2, captured.size());
		assertEquals(false, captured.get(1).getRunning());
		assertEquals((Integer)1, captured.get(1).getExitCode());
		
		
	}}