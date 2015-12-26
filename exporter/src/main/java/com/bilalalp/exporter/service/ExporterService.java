package com.bilalalp.exporter.service;

import java.io.IOException;

public interface ExporterService {

    void export(Long requestId) throws IOException;
}
