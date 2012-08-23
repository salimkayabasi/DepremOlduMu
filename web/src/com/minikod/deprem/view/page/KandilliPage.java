package com.minikod.deprem.view.page;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.jsoup.Jsoup;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.minikod.deprem.PMF;
import com.minikod.deprem.model.Deprem;

@ManagedBean(name = "pg_Kandilli")
@SuppressWarnings("unchecked")
@ViewScoped
public class KandilliPage extends AbstractPage {

	private static final long serialVersionUID = 1L;

	private String HTMLResult;
	private DateFormat formatter;
	private DateFormat formatterTR;
	private long cursor = -1;

	public KandilliPage() {
		//followThem();
		getData();
	}

	void getData() {
		try {
			HTMLResult = "";
			getData(new URL(
					"http://www.koeri.boun.edu.tr/scripts/sondepremler.asp"));
		} catch (Exception e) {
			e.printStackTrace();
			HTMLResult = "";
		}

		if (!HTMLResult.isEmpty()) {
			List<String> lstDepremResult = Arrays
					.asList(HTMLResult.split("\n")).subList(31, 2031);
			if (getDeprem() != null) {
				insertData(convert(lstDepremResult));
				return;
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();

			try {
				Query qDeprem = pm.newQuery(Deprem.class);
				qDeprem.setOrdering("time desc");
				qDeprem.setRange(0, 1);

				try {
					List<Deprem> results = (List<Deprem>) qDeprem.execute();
					if (!results.isEmpty()) {
						setDeprem(results.get(0));
					} else {
						setDeprem(null);
					}
					insertData(convert(lstDepremResult));
				} finally {
					qDeprem.closeAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				pm.close();
			}
		}
	}

	private List<Deprem> convert(List<String> lstDepremString) {

		Locale tr = new Locale("tr");
		formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		formatterTR = new SimpleDateFormat("dd MMM HH:mm", tr);
		List<Deprem> lstDeprem = new ArrayList<Deprem>();
		for (String dprm : lstDepremString) {
			Deprem d = new Deprem();
			try {
				String tarih = dprm.substring(0, 19);
				d.setTime(formatter.parse(tarih));

				if (getDeprem() != null
						&& !d.getTime().after(getDeprem().getTime()))
					break;

				List<String> lstDprm = Arrays.asList(dprm.replaceAll("\\s+",
						" ").split(" "));
				d.setLat(Double.parseDouble(lstDprm.get(2)));
				d.setLng(Double.parseDouble(lstDprm.get(3)));
				d.setDepth(Double.parseDouble(lstDprm.get(4)));
				try {
					d.setMagnitudeMD(Double.parseDouble(lstDprm.get(5)));
				} catch (NumberFormatException e) {
					d.setMagnitudeMD((double) -1);
				}
				try {
					d.setMagnitudeML(Double.parseDouble(lstDprm.get(6)));
				} catch (NumberFormatException e) {
					d.setMagnitudeML((double) -1);
				}
				try {
					d.setMagnitudeMW(Double.parseDouble(lstDprm.get(7)));
				} catch (NumberFormatException e) {
					d.setMagnitudeMW((double) -1);
				}
				d.setRegion(dprm.substring(71, 121).trim());
				lstDeprem.add(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (lstDeprem.size() == 0)
			return null;
		else {
			Collections.reverse(lstDeprem);
			setDeprem(lstDeprem.get(lstDeprem.size() - 1));
			String harita = "";
			String twit = "";
			Double buyukluk = 0.0;
			Twitter twitter = new TwitterFactory().getInstance();
			for (Deprem deprem1 : lstDeprem) {

				buyukluk = Math.max(
						Math.max(deprem1.getMagnitudeMD(),
								deprem1.getMagnitudeML()),
						deprem1.getMagnitudeMS());
				harita = "http://maps.google.com/maps?q=" + deprem1.getLat()
						+ "," + deprem1.getLng();

				twit = formatterTR.format(deprem1.getTime()) + " "
						+ deprem1.getRegion() + " bölgesinde " + buyukluk
						+ " büyüklüğünde #deprem oldu " + " #kandilli";
				try {
					twitter.updateStatus(twit + " " + harita);
				} catch (TwitterException e) {
					try {
						twitter.updateStatus(twit);
					} catch (TwitterException e1) {

					}
				}
			}
			return lstDeprem;
		}
	}

	@SuppressWarnings("unused")
	private void followThem() {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			IDs lstID = twitter.getFollowersIDs("kandilli_info", cursor);
			for (long id : lstID.getIDs()) {
				twitter.createFriendship(id);
			}
			cursor = lstID.getNextCursor();
			followThem();
		} catch (TwitterException e2) {
		}

	}

	private void insertData(List<Deprem> lstDeprem) {
		if (lstDeprem == null)
			return;
		else {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistentAll(lstDeprem);
			} catch (Exception e) {
			} finally {
				pm.close();
			}
		}
	}

	private void getData(URL url) {
		try {
			HTMLResult = Jsoup.parse(url.openStream(), "CP1254",
					url.toExternalForm()).toString();
			HTMLResult = HTMLResult.replaceAll("&Ccedil;", "Ç");
			HTMLResult = HTMLResult.replaceAll("&Ouml;", "Ö");
			HTMLResult = HTMLResult.replaceAll("&Uuml;", "Ü");
			HTMLResult = HTMLResult.replaceAll("&ccedil;", "ç");
			HTMLResult = HTMLResult.replaceAll("&ouml;", "ö");
			HTMLResult = HTMLResult.replaceAll("&uuml;", "ü");
		} catch (IOException e) {
			HTMLResult = "";
		}
	}

	public String getHTMLResult() {
		return HTMLResult;
	}

	public void setHTMLResult(String HTMLResult) {
		this.HTMLResult = HTMLResult;
	}

}
