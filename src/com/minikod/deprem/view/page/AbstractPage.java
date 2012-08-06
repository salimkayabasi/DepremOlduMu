package com.minikod.deprem.view.page;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import com.minikod.deprem.model.Deprem;

@SessionScoped
public abstract class AbstractPage implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Deprem deprem;

	public AbstractPage() {

	}

	public static Deprem getDeprem() {
		return deprem;
	}

	public static void setDeprem(Deprem deprem) {
		AbstractPage.deprem = deprem;
	}

}
