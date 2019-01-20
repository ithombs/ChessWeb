package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thombs.ChessWeb.Models.ActivityAudit;

@Service("ActivityAuditService")
public class ActivityAuditServiceImpl implements ActivityAuditService{
	
	@Autowired
	ActivityAuditDAO activityAuditDAO;

	@Override
	@Transactional
	public ActivityAudit findByID(int id) {
		return activityAuditDAO.findByID(id);
	}

	@Override
	@Transactional
	public List<ActivityAudit> findByActivityName(String activityName) {
		return activityAuditDAO.findByActivityName(activityName);
	}

	@Override
	@Transactional
	public ActivityAudit saveActivityAudit(ActivityAudit audit) {
		return activityAuditDAO.saveActivityAudit(audit);
	}

	@Override
	@Transactional
	public List<ActivityAudit> getAllActivityAudits() {
		return activityAuditDAO.getAllActivityAudits();
	}
	
}
