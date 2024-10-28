package cl.lanixerp.pca.gui;

import javax.swing.JEditorPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class PageLoader  extends SwingWorker<Void, Void> { 

	private JEditorPane editorPane;
	private JProgressBar progressBar;
	private String url;

	public PageLoader(JEditorPane editorPane, JProgressBar progressBar) {
		this.editorPane = editorPane;
		this.progressBar = progressBar;
	}

	public void execute(String url) {
		// LanixERP.logger.info("Aviso:" + url);
		this.url = url;
		//progressBar.setVisible(true);
		progressBar.setIndeterminate(true);
		execute();
	}

	@Override
	protected Void doInBackground() throws Exception {
		// LanixERP.logger.info("...Cargando");
		editorPane.setPage(url);
		// LanixERP.logger.info("Cargado");
		return null;
	}

	@Override
	protected void done() {
		progressBar.setIndeterminate(false);
		//progressBar.setVisible(false);
	}
}
