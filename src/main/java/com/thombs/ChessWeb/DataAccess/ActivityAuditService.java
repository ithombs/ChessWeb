package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.ActivityAudit;

public interface ActivityAuditService {
	public ActivityAudit findByID(int id);
	public List<ActivityAudit> findByActivityName(String activityName);
	public ActivityAudit saveActivityAudit(ActivityAudit audit);
	public List<ActivityAudit> getAllActivityAudits();
}
