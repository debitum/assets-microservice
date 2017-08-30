package com.debitum.assets.domain.model.dashboard;


import java.util.UUID;

public interface DashboardService {

    Dashboard overview(UUID userId);
}
