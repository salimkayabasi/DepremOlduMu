package com.minikod.deprem.resource.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FacesUtil {
	public static Object getRequestParameter(String key) {
		return ((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getParameter(key);
	}

	public static void navigate(String outcome) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getNavigationHandler()
				.handleNavigation(context, null, outcome);
	}

	public static void redirect(String uri) {
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}