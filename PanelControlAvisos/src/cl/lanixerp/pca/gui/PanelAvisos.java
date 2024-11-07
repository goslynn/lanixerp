package cl.lanixerp.pca.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import cl.lanixerp.pca.PCA;
import cl.lanixerp.pca.PCA.Argumento;
import cl.lanixerp.pca.modelo.Aviso;
import info.clearthought.layout.TableLayout;

public class PanelAvisos extends JFrame {

	private DefaultListModel listModel;
	private JList<Aviso> jList;
	private JEditorPane editorPane;

	private JLabel lblTarget;
	private JComboBox<String> cmbTartget;

	private JLabel lblURL;
	private JTextField txtURL;

	private JLabel lblTimeout;
	private JComboBox<Integer> cmbTimeout;

	private JButton btnVer;
	private JToggleButton btnPlay;
	private boolean trabajando;

	private CargadorAvisos cav;
	private JButton removeButton;
	private JButton refreshButton;
	private JButton addButton;
//        private JButton testButton;

	private JProgressBar progress;

	URL urlLocal = this.getClass().getResource("/cl/lanixerp/pca/avisofijo/avisofijo.html");

	public PanelAvisos() {

		setTitle("Panel Control Avisos " + 
                    (PCA.is(Argumento.local) ? "LOCAL" : 
                    (PCA.is(Argumento.dev) ? "DEV" : "PROD")));
		setSize(900, 600);
		setMinimumSize(getSize());
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		cav = new CargadorAvisos();

		// Crear el modelo de la lista y el JList
		listModel = new DefaultListModel();
		jList = new JList(listModel);

		JPanel panelVisor = new JPanel();
		TableLayout tl = new TableLayout(new double[] { 0.5, 50, 50, 500.0, 50.0, 50.0, 0.5 },
				new double[] { 0.5, 15, 40, 10, 400.0, 40, 0.5 });
		panelVisor.setLayout(tl);

		// Añadir el JList dentro de un JScrollPane
		JScrollPane listScrollPane = new JScrollPane(jList);
		listScrollPane.setPreferredSize(new Dimension(150, 0));
		JLabel lblLista = new JLabel("Publicaciones:");

		// Crear el JEditorPane para mostrar HTML
		editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {

						String strUrl = e.getDescription();
						{
							try {
								Desktop.getDesktop().browse(e.getURL().toURI());
							} catch (Exception expt) {
								String os = System.getProperty("os.name").toLowerCase();
								if (os.contains("win")) {
									Runtime.getRuntime()
											.exec("rundll32 url.dll,FileProtocolHandler " + e.getURL().toString());
								} else if (os.contains("mac")) {
									Runtime.getRuntime().exec("open " + e.getURL().toString());
								} else if (os.contains("nix") || os.contains("nux")) {
									Runtime.getRuntime().exec("open " + e.getURL().toString());
								}
							}
						}

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		JScrollPane editorScrollPane = new JScrollPane(editorPane);

		lblURL = new JLabel("URL");
		txtURL = new JTextField();

		lblTarget = new JLabel("Target");
		cmbTartget = new JComboBox<String>(
				new String[] { "TODOS", "4.0", "5.0", "6.0", "POS2.1", "CRM", "LMS", "REMU", });

		lblTimeout = new JLabel("Seg");
		cmbTimeout = new JComboBox<Integer>(new Integer[] { 5, 10, 15, 20, 30, 60 });

		btnVer = new JButton("Ir");
		btnVer.setToolTipText("Ver URL");
		ActionListener alVer = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String url = txtURL.getText();
				Runnable r= new Runnable() {
					@Override
					public void run() {
							progress.setIndeterminate(true);
							jList.clearSelection();
							   String url = txtURL.getText();
							   final String urlx;
						        if (!url.startsWith("http")) {
						            urlx = "http://" + url;
						        }else {
						        	urlx = url;
						        }

						        // Ejecutar la solicitud HTTP en segundo plano
						        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						        	private boolean success = true; 
						            @Override
						            protected Void doInBackground() throws Exception {
						                try {
						                    URL u = new URL(urlx);
						                    HttpURLConnection huc = (HttpURLConnection) u.openConnection();
						                    huc.setConnectTimeout(3000); // 3 segundos
						                    huc.setReadTimeout(5000); // 5 segundos
						                    huc.setRequestMethod("HEAD");
						                    int responseCode = huc.getResponseCode();
						                    System.out.println("ResponseCode:"+responseCode);
						                    success = true;
						                } catch (IOException ex) {
						                	System.out.println("Fail ResponseCode");
						                	ex.printStackTrace();
						                    success = false;
						                }
						                return null;
						            }

						            @Override
						            protected void done() {
						                try {
						                	if (success) {
						                		System.out.println("Cargando:"+urlx);
						                		editorPane.setPage(urlx);						                		
						                	}
						                	else {
						                		System.out.println("Cargando:"+urlLocal);
						                		editorPane.setPage(urlLocal);
						                		JOptionPane.showMessageDialog(null, "Error al conectar con la URL: " + urlx, "Error", JOptionPane.ERROR_MESSAGE);
						                	}
						                } catch (IOException ex) {
												ex.printStackTrace();
						                }
						                progress.setIndeterminate(false);
						            }
						        };

						        // Iniciar el trabajador en segundo plano
						        worker.execute();
						
						
					}
				};
				
				new Thread(r, "Ir a "+url).start();;
				
				
			}
		};

		btnVer.addActionListener(alVer);
		txtURL.addActionListener(alVer);

		btnPlay = new JToggleButton(">");
		btnPlay.setToolTipText("Inicia Ronda de Avisos");
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rondaAvisos();
			}
		});
                
//                testButton = new JButton("a");
//                testButton.setToolTipText("Probar Conexion DB");
//                testButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//                            System.out.println("Hola Mundo");
//                            cav.test();
//			}
//		});

		// Crear botón para eliminar elementos de la lista
		removeButton = new JButton("-");
		removeButton.setToolTipText("Quitar Aviso");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = jList.getSelectedIndex();
				if (selectedIndex != -1) {
					Aviso a = jList.getSelectedValue();
					int op = JOptionPane.showConfirmDialog(PanelAvisos.this,
							"Se eliminara aviso :\n\n" + "ID    :" + a.getId() + "\n" + "URL   :" + a.getUrl() + "\n"
									+ "TARGET:" + a.getTarget() + "\n" + "TIEMPO:" + a.getTiempo_ms() + "\n"
									+ "\n\n ¿Desea Contniua?",
							"Quitar Aviso", JOptionPane.YES_NO_OPTION);
					if (op == JOptionPane.YES_OPTION) {
						cav.borrarAviso(a.getId());
					}
				} else {
					JOptionPane.showMessageDialog(PanelAvisos.this,
							"Ningun aviso seleccionado.\nPara eliminar un aviso, seleccionar de la lista.");
				}

				cargarAvisos();
				txtURL.setText("");
			}
		});

		refreshButton = new JButton("R");
		refreshButton.setToolTipText("Refrescar Lista de Avisos");
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cargarAvisos();
				int sx = listModel.getSize(); 
				if (sx>0) {
					JOptionPane.showMessageDialog(PanelAvisos.this,
							"Se carg"+(sx==1?"ó ":"aron ")+listModel.getSize()+" publicaci"+(sx==1?"ón":"ones."));	
				}else {
					JOptionPane.showMessageDialog(PanelAvisos.this,
							"No se encontraron publicaciones.");
				}
				
			}
		});

		// Crear campo de texto y botón para agregar elementos a la lista

		addButton = new JButton("+");
		addButton.setToolTipText("Agregar URL Visible");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Aviso a = new Aviso();

				a.setId(cav.getNuevoID());
				a.setTarget("" + cmbTartget.getSelectedItem());
				a.setUrl(txtURL.getText());
				Integer tm = (Integer) cmbTimeout.getSelectedItem();
				a.setTiempo_ms(tm * 1000);

				if (a.getUrl() == null || a.getUrl().isEmpty() || !a.getUrl().startsWith("http")) {
					JOptionPane.showMessageDialog(PanelAvisos.this, "Se requiere una URL Valida (http | https).");
					txtURL.requestFocusInWindow();
					return;
				}

				for (int j = 0; j < listModel.getSize(); j++) {

					Aviso ax = (Aviso) listModel.get(j);
					if (ax.getUrl().equals(a.getUrl())) {
						// JOptionPane.showMessageDialog(PanelAvisos.this, "URL:" + a.getUrl() + "
						// repetida. Se Omite.");
						// txtURL.requestFocusInWindow();
						int op = JOptionPane.showConfirmDialog(PanelAvisos.this,
								"Reemplazar Aviso Existente:\n\n" + "Original:\n" + "-ID    :" + ax.getId() + "\n"
										+ "-URL   :" + ax.getUrl() + "\n" + "-TARGET:" + ax.getTarget() + "\n"
										+ "-TIEMPO:" + ax.getTiempo_ms() + "\n" + "\nReemplaza por:\n" + "-ID    :"
										+ a.getId() + "\n" + "-URL   :" + a.getUrl() + "\n" + "-TARGET:" + a.getTarget()
										+ "\n" + "-TIEMPO:" + a.getTiempo_ms() + "\n"

										+ "\n\n ¿Reemplazar?",
								"Reemplazo de Aviso", JOptionPane.YES_NO_OPTION);
						if (op == JOptionPane.YES_OPTION) {
							cav.borrarAviso(ax.getId());
						} else {
							return;
						}
					}
				}

				cav.enviarAviso(a);
				cargarAvisos();
			}
		});

		// Añadir un listener para cambiar el contenido del JEditorPane cuando se
		// seleccione un elemento de la lista
		jList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				Aviso selectedValue = jList.getSelectedValue();

				if (selectedValue != null) {
					try {
						cmbTartget.setSelectedItem(selectedValue.getTarget());
						txtURL.setText(selectedValue.getUrl());
						editorPane.setPage(selectedValue.getUrl());
						int tout = selectedValue.getTiempo_ms() / 1000;
						cmbTimeout.setSelectedItem(tout);
					} catch (IOException e1) {
						editorPane.setText("<html><h1>NO ENCONTRADO</h1> <p>" + selectedValue.getUrl() + "</p></html>");
					}
					// editorPane.setText("<html><h1>" + selectedValue + "</h1></html>");
				}
			}
		});

		// Layout principal
		setLayout(new BorderLayout());

		// Panel para agregar/eliminar elementos
		JPanel listControlPanel = new JPanel();
		progress = new JProgressBar();
		listControlPanel.setLayout(new BorderLayout());
		listControlPanel.add(lblLista, BorderLayout.NORTH);
		listControlPanel.add(listScrollPane, BorderLayout.CENTER);

		panelVisor.add("1, 1, 2, 1", lblTarget);
		panelVisor.add("3, 1 ", lblURL);
		panelVisor.add("4, 1 ", lblTimeout);

		panelVisor.add("1, 2, 2, 1 ", cmbTartget);
		panelVisor.add("3, 2 ", txtURL);
		panelVisor.add("4, 2 ", cmbTimeout);
		panelVisor.add("5, 2 ", btnVer);

		panelVisor.add("1, 3, 5, 3", progress);
		panelVisor.add("1, 4, 5, 4", editorScrollPane);

		panelVisor.add("1, 5 ", refreshButton);
		panelVisor.add("2, 5 ", btnPlay);
//		panelVisor.add("3, 6 ", testButton);
		panelVisor.add("4, 5 ", removeButton);
		panelVisor.add("5, 5 ", addButton);

		// Agregar los componentes al JFrame
		add(listControlPanel, BorderLayout.WEST);
		add(panelVisor, BorderLayout.CENTER);

		cargarAvisos();
	}

	protected void setEnabledControl(boolean f) {
		btnPlay.setToolTipText(f ? "Inicia Ronda de Avisos" : "Detiene Ronda Avisos");
		removeButton.setEnabled(f);
		refreshButton.setEnabled(f);
		addButton.setEnabled(f);
		txtURL.setEnabled(f);
		jList.setEnabled(f);
		btnVer.setEnabled(f);
		cmbTartget.setEnabled(f);
		cmbTimeout.setEnabled(f);

	}

	protected void rondaAvisos() {
		Runnable r = new Runnable() {
			@Override
			public void run() {

				setEnabledControl(false);
				if (btnPlay.isSelected()) {
					ArrayList<Aviso> avisosLinks = cav.cargarAvisos();
					while (btnPlay.isSelected()) {
						try {
							if (avisosLinks.size() > 0) {
								System.out.println("0->fijo");
								editorPane.setPage(urlLocal);
								Thread.sleep(10000);
								int ax = 0;
								for (Aviso a : avisosLinks) {
									System.out.println((ax++) + "->" + a.getUrl() + "[" + a.getTiempo_ms() + " ms]");
									if (!btnPlay.isSelected()) {
										break;
									}
									new PageLoader(editorPane, progress).execute(a.getUrl());
									try {
										Thread.sleep(a.getTiempo_ms());
									} catch (InterruptedException e) {

									}

								}
							} else {
								JOptionPane.showMessageDialog(PanelAvisos.this, "No hay aviso(s) publicados.");
								btnPlay.setSelected(false);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}

					}
				}
				setEnabledControl(true);
			}
		};

		new Thread(r, "Ronda de Avisos").start();

	}

	private void cargarAvisos() {

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					editorPane.setPage(urlLocal);
				} catch (IOException e) {
					e.printStackTrace();
				}

				listModel.clear();
				ArrayList<Aviso> avs = cav.cargarAvisos();
				for (Aviso a : avs) {
					listModel.addElement(a);
				}

			}
		};

		new Thread(r, "CargaAvisos").start();
		;

	}

}
