package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import com.thombs.ChessWeb.Models.ActivityAudit;
import com.thombs.ChessWeb.Models.User.User;

public interface ActivityAuditDAO {
	public ActivityAudit findByID(int id);
	public List<ActivityAudit> findByActivityName(String activityName);
	public ActivityAudit saveActivityAudit(ActivityAudit audit);
	public List<ActivityAudit> getAllActivityAudits();
}
