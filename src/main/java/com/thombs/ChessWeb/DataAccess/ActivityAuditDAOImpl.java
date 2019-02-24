package com.thombs.ChessWeb.DataAccess;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thombs.ChessWeb.Models.ActivityAudit;
import com.thombs.ChessWeb.Models.User.User;

@Repository
public class ActivityAuditDAOImpl implements ActivityAuditDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public ActivityAudit findByID(int id) {
		Session session = sessionFactory.getCurrentSession();
		ActivityAudit aa = (ActivityAudit)session.get(ActivityAudit.class, id);
		return aa;
	}

	@Override
	public List<ActivityAudit> findByActivityName(String activityName) {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ActivityAudit where activityName = :activityName");
		q.setParameter("activityAudit", activityName);
		List<ActivityAudit> aa = q.getResultList();
		return aa;
	}

	@Override
	public ActivityAudit saveActivityAudit(ActivityAudit audit) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(audit);
		return audit;
	}

	@Override
	public List<ActivityAudit> getAllActivityAudits() {
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createQuery("from ActivityAudit");
		List<ActivityAudit> aa = q.getResultList();
		return aa;
	}
	
}
