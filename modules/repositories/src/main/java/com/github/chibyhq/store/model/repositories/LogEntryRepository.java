package com.github.chibyhq.store.model.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.chibyhq.playar.model.LogEntry;

import org.springframework.data.repository.CrudRepository;

public interface LogEntryRepository extends PagingAndSortingRepository<LogEntry, String>, CrudRepository<LogEntry, String> {

}
