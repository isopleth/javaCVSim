package edu.mit.lcp;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

public class ImagePanel6C extends ImagePanel {

    private final javax.swing.JTabbedPane GraphicsTab;
    private final javax.swing.JPanel anatomicpanel;
    private final javax.swing.JLabel circtitle;
    private final javax.swing.JLabel circuit;
    private final javax.swing.JLabel circuitmodel;
    private final javax.swing.JPanel circuitpanel;
    private final javax.swing.JTextArea desc;
    private final javax.swing.JScrollPane descpane;
    private final javax.swing.JLabel desctitle;
    private final javax.swing.JLabel flow;
    private final javax.swing.JLabel flowleft;
    private final javax.swing.JLabel flowright;
    private final javax.swing.JLabel hspace;
    private final javax.swing.JLabel hspace2;
    private final javax.swing.JLabel leftheart;
    private final javax.swing.JLabel llung;
    private final javax.swing.JLabel periphartery2;
    private final javax.swing.JLabel periphartey1;
    private final javax.swing.JLabel periphmicro;
    private final javax.swing.JLabel periphveins;
    private final javax.swing.JLabel periphveins2;
    private final javax.swing.JLabel periphviens3;
    private final javax.swing.JLabel pulartery;
    private final javax.swing.JLabel pulartery3;
    private final javax.swing.JLabel pulartey2;
    private final javax.swing.JLabel pulmicro;
    private final javax.swing.JLabel pulspace;
    private final javax.swing.JLabel pulveins;
    private final javax.swing.JLabel rightheart;
    private final javax.swing.JLabel rlung1;
    private final javax.swing.JLabel toplung;
    
    public ImagePanel6C() {
	GraphicsTab = new javax.swing.JTabbedPane();
        anatomicpanel = new javax.swing.JPanel();
        rlung1 = new javax.swing.JLabel();
        toplung = new javax.swing.JLabel();
        pulartery = new javax.swing.JLabel();
        pulmicro = new javax.swing.JLabel();
        pulspace = new javax.swing.JLabel();
        periphveins = new javax.swing.JLabel();
        periphveins2 = new javax.swing.JLabel();
        pulartey2 = new javax.swing.JLabel();
        periphviens3 = new javax.swing.JLabel();
        pulartery3 = new javax.swing.JLabel();
        hspace = new javax.swing.JLabel();
        periphartey1 = new javax.swing.JLabel();
        rightheart = new javax.swing.JLabel();
        pulveins = new javax.swing.JLabel();
        leftheart = new javax.swing.JLabel();
        hspace2 = new javax.swing.JLabel();
        llung = new javax.swing.JLabel();
        periphartery2 = new javax.swing.JLabel();
        periphmicro = new javax.swing.JLabel();
        descpane = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
	desc.setEditable(false);
        circuit = new javax.swing.JLabel();
        circtitle = new javax.swing.JLabel();
        desctitle = new javax.swing.JLabel();
        flowright = new javax.swing.JLabel();
        flow = new javax.swing.JLabel();
        flowleft = new javax.swing.JLabel();
        circuitpanel = new javax.swing.JPanel();
        circuitmodel = new javax.swing.JLabel();

	GraphicsTab.setPreferredSize(new java.awt.Dimension(595, 520));
        anatomicpanel.setBackground(new java.awt.Color(255, 255, 255));
        anatomicpanel.setMaximumSize(new java.awt.Dimension(600, 520));
        anatomicpanel.setPreferredSize(new java.awt.Dimension(590, 515));
        rlung1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/1.jpg")));

        toplung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/2.jpg")));

        pulartery.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/3.jpg")));
        pulartery.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    pularteryMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    pularteryMouseExited(evt);
		}
	    });

        pulmicro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/4.jpg")));
        pulmicro.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
            	public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Microcirculation");
		}
                    @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    pulmicroMouseEntered(evt);
		    highlightParameterTable("Pulmonary Microcirculation");
		}
                    @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    pulmicroMouseExited(evt);
		}
	    });

        pulspace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/5.jpg")));

        periphveins.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/8.jpg")));
        periphveins.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Systemic Veins");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphveinsMouseEntered(evt);
		    highlightParameterTable("Systemic Veins");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphveinsMouseExited(evt);
		}
	    });

        periphveins2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/9.jpg")));
        periphveins2.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Systemic Veins");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphveins2MouseEntered(evt);
		    highlightParameterTable("Systemic Veins");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphveins2MouseExited(evt);
		}
	    });

        pulartey2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/10a.jpg")));
        pulartey2.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    pulartey2MouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    pulartey2MouseExited(evt);
		}
	    });

        periphviens3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/10d.jpg")));
        periphviens3.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Veins");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphviens3MouseEntered(evt);
		    highlightParameterTable("Pulmonary Veins");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphviens3MouseExited(evt);
		}
	    });

        pulartery3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/10c.jpg")));
        pulartery3.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    pulartery3MouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    pulartery3MouseExited(evt);
		}
	    });

        hspace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/11.jpg")));

        periphartey1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/5new.jpg")));
        periphartey1.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Systemic Arteries");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphartey1MouseEntered(evt);
		    highlightParameterTable("Systemic Arteries");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphartey1MouseExited(evt);
		}
	    });

        rightheart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/13.jpg")));
        rightheart.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Right Heart");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    rightheartMouseEntered(evt);
		    highlightParameterTable("Right Heart");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    rightheartMouseExited(evt);
		}
	    });

        pulveins.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/6.jpg")));
        pulveins.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Pulmonary Veins");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    pulveinsMouseEntered(evt);
		    highlightParameterTable("Pulmonary Veins");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    pulveinsMouseExited(evt);
		}
	    });

        leftheart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/14.jpg")));
        leftheart.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Left Heart");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    leftheartMouseEntered(evt);
		    highlightParameterTable("Left Heart");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    leftheartMouseExited(evt);
		}
	    });

        hspace2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/17.jpg")));

        llung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/15.jpg")));

        periphartery2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/16.jpg")));
        periphartery2.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    filterParameterTable("Systemic Arteries");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphartery2MouseEntered(evt);
		    highlightParameterTable("Systemic Arteries");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphartery2MouseExited(evt);
		}
	    });

        periphmicro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/18.jpg")));
        periphmicro.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
		public void mouseClicked(java.awt.event.MouseEvent evt) {
		    if (CVSim.simulationModelName.equals(CVSim.MODEL_6C))
			filterParameterTable("Systemic Microcirculation");
		}
                @Override
		public void mouseEntered(java.awt.event.MouseEvent evt) {
		    periphmicroMouseEntered(evt);
		    if (CVSim.simulationModelName.equals(CVSim.MODEL_6C))
			highlightParameterTable("Systemic Microcirculation");
		}
                @Override
		public void mouseExited(java.awt.event.MouseEvent evt) {
		    periphmicroMouseExited(evt);
		}
	    });

        descpane.setBorder(null);
        desc.setColumns(20);
        desc.setLineWrap(true);
        desc.setRows(5);
        desc.setWrapStyleWord(true);
        descpane.setViewportView(desc);

        circtitle.setFont(new java.awt.Font("Tahoma", 1, 12));

        desctitle.setFont(new java.awt.Font("Tahoma", 1, 12));

        flow.setFont(new java.awt.Font("Tahoma", 0, 10));

        GroupLayout anatomicpanelLayout = new GroupLayout(anatomicpanel);
        anatomicpanel.setLayout(anatomicpanelLayout);
        anatomicpanelLayout.setHorizontalGroup(
            anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(anatomicpanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(periphmicro))
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addComponent(rlung1)
                        .addGap(0, 0, 0)
                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(toplung)
                                .addGap(0, 0, 0)
                                .addComponent(llung))
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(pulartery)
                                .addGap(0, 0, 0)
                                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(pulmicro)
                                    .addComponent(pulspace))
                                .addGap(0, 0, 0)
                                .addComponent(pulveins)
                                .addGap(0, 0, 0)
                                .addComponent(periphartery2))))
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addComponent(periphveins)
                        .addGap(0, 0, 0)
                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(periphveins2)
                                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                                        .addComponent(pulartey2)
                                        .addGap(0, 0, 0)
                                        .addComponent(hspace)
                                        .addGap(0, 0, 0)
                                        .addComponent(periphartey1))
                                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                                        .addGap(0, 0, 0)
                                        .addComponent(periphviens3)
                                        .addGap(0, 0, 0)
                                        .addComponent(pulartery3))))
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(rightheart)
                                .addGap(0, 0, 0)
                                .addComponent(leftheart))
                            .addComponent(hspace2))))
                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(flowleft)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flow)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flowright))
                            .addComponent(circtitle)
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(circuit))))
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descpane, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE))
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(desctitle)))
                .addGap(4, 4, 4))
        );
        anatomicpanelLayout.setVerticalGroup(
            anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(anatomicpanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(rlung1)
                                .addGap(0, 0, 0)
                                .addComponent(periphveins))
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(toplung)
                                .addGap(0, 0, 0)
                                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(anatomicpanelLayout.createSequentialGroup()
                                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(pulartery)
                                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                                .addComponent(pulmicro)
                                                .addGap(0, 0, 0)
                                                .addComponent(pulspace)))
                                        .addGap(0, 0, 0)
                                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(periphveins2)
                                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                                .addComponent(pulartey2)
                                                .addGap(0, 0, 0)
                                                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(periphviens3)
                                                    .addComponent(pulartery3)))
                                            .addComponent(hspace)
                                            .addComponent(periphartey1)))
                                    .addComponent(pulveins))
                                .addGap(0, 0, 0)
                                .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(rightheart)
                                    .addComponent(leftheart))
                                .addGap(0, 0, 0)
                                .addComponent(hspace2))
                            .addGroup(anatomicpanelLayout.createSequentialGroup()
                                .addComponent(llung)
                                .addGap(0, 0, 0)
                                .addComponent(periphartery2)))
                        .addGap(0, 0, 0)
                        .addComponent(periphmicro)
                        .addContainerGap(487, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, anatomicpanelLayout.createSequentialGroup()
                        .addComponent(desctitle)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descpane, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(circtitle)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(circuit)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(anatomicpanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(flow)
                            .addComponent(flowleft)
                            .addComponent(flowright))
                        .addGap(28, 28, 28))))
        );
        GraphicsTab.addTab("Anatomical Overview", anatomicpanel);

        circuitpanel.setBackground(new java.awt.Color(255, 255, 255));
        circuitmodel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/cvsim_35.gif")));

        GroupLayout circuitpanelLayout = new GroupLayout(circuitpanel);
        circuitpanel.setLayout(circuitpanelLayout);
        circuitpanelLayout.setHorizontalGroup(
            circuitpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circuitpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(circuitmodel)
                .addContainerGap(568, Short.MAX_VALUE))
        );
        circuitpanelLayout.setVerticalGroup(
            circuitpanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(circuitpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(circuitmodel)
                .addContainerGap(476, Short.MAX_VALUE))
        );
        GraphicsTab.addTab("Circuit Overview", circuitpanel);

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(GraphicsTab, GroupLayout.PREFERRED_SIZE, 583, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(GraphicsTab, GroupLayout.PREFERRED_SIZE, 512, GroupLayout.PREFERRED_SIZE)
        );

    } // end constructor
    
    private void pulmicroMouseExited(java.awt.event.MouseEvent evt) {                                     
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                    

    private void periphveins2MouseExited(java.awt.event.MouseEvent evt) {                                         
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                        

    private void periphartey1MouseExited(java.awt.event.MouseEvent evt) {                                         
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                        

    private void periphmicroMouseExited(java.awt.event.MouseEvent evt) {                                        
        circuit.setIcon(null);
        desctitle.setText(null);
        desc.setText(null);
        circtitle.setText(null);
        flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                       

    private void periphartery2MouseExited(java.awt.event.MouseEvent evt) {                                          
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                         

    private void pulveinsMouseExited(java.awt.event.MouseEvent evt) {                                     
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                    

    private void periphviens3MouseExited(java.awt.event.MouseEvent evt) {                                         
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                        

    private void pulartery3MouseExited(java.awt.event.MouseEvent evt) {                                       
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                      

    private void pulartey2MouseExited(java.awt.event.MouseEvent evt) {                                      
        circuit.setIcon(null);
        desctitle.setText(null);
        desc.setText(null);
        circtitle.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                     

    private void pularteryMouseExited(java.awt.event.MouseEvent evt) {                                      
        circuit.setIcon(null);
        desctitle.setText(null);
        desc.setText(null);
        circtitle.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                     

    private void periphveinsMouseExited(java.awt.event.MouseEvent evt) {                                        
        circuit.setIcon(null);
        desctitle.setText(null);
        circtitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                       

    private void rightheartMouseExited(java.awt.event.MouseEvent evt) {                                       
        circuit.setIcon(null);
        circtitle.setText(null);
        desctitle.setText(null);
        desc.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                      

    private void leftheartMouseExited(java.awt.event.MouseEvent evt) {                                      
        circuit.setIcon(null);
        desctitle.setText(null);
        desc.setText(null);
        circtitle.setText(null);
         flowright.setIcon(null);
        flowleft.setIcon(null);
        flow.setText(null);
    }                                     

    private void pulveinsMouseEntered(java.awt.event.MouseEvent evt) {                                      
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pulV.gif")));
        desctitle.setText("Pulmonary Veins");
        desc.setText("The pulmonary veins deliver oxygenated blood to the left heart.");
        circtitle.setText("Circuit Representation");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/oxyright.jpg")));
        flow.setText("Blood Flow");
    }                                     

    private void pulmicroMouseEntered(java.awt.event.MouseEvent evt) {                                      
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/resistor.gif")));
        desctitle.setText("Pulmonary Microcirculation");
        circtitle.setText("Circuit Representation");
        desc.setText("In the pulmonary microcirculation oxygen exchange takes place across the membrane separating pulmonary alveoli from the pulmonary capillary network. The pulmonary microcirculation thus receives de-oxygenated blood from the pulmonary arteries and returns oxygenated blood to the pulmonary veins.");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/oxyrightde.jpg")));
        flow.setText("Blood Flow");
    }                                     

    private void pularteryMouseEntered(java.awt.event.MouseEvent evt) {                                       
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pulC.gif")));
        desctitle.setText("Pulmonary Arteries");
        circtitle.setText("Circuit Representation");
        desc.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
         flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyright.jpg")));
        flow.setText("Blood Flow");
    }                                      

    private void pulartey2MouseEntered(java.awt.event.MouseEvent evt) {                                       
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pulC.gif")));
        desctitle.setText("Pulmonary Arteries");
        circtitle.setText("Circuit Representation");
        desc.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyright.jpg")));
        flow.setText("Blood Flow");
    }                                      

    private void pulartery3MouseEntered(java.awt.event.MouseEvent evt) {                                        
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pulC.gif")));
        desctitle.setText("Pulmonary Arteries");
        circtitle.setText("Circuit Representation");
        desc.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyright.jpg")));
        flow.setText("Blood Flow");
    }                                       

    private void leftheartMouseEntered(java.awt.event.MouseEvent evt) {                                       
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/leftheart.gif")));
        desctitle.setText("Left Heart");
        circtitle.setText("Circuit Representation");
        desc.setText("During diastole, the left heart receives oxygenated blood from the pulmonary veins; during systole, it generates the required pressure to propel the blood into the systemic arteries.");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/oxyright.jpg")));
        flow.setText("Blood Flow");
    }                                      

    private void rightheartMouseEntered(java.awt.event.MouseEvent evt) {                                        
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/rightheart.gif")));
        desctitle.setText("Right Heart");
        circtitle.setText("Circuit Representation");
        desc.setText("During diastole, the right heart receives de-oxygenated blood from the systemic veins; during systole, it generates the required pressure to propel the blood into the pulmonary arteries.");
        flowright.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyright.jpg")));
        flow.setText("Blood Flow");
    }                                       

    private void periphmicroMouseEntered(java.awt.event.MouseEvent evt) {                                         
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/resistor.gif")));
        desctitle.setText("Systemic Microcirculation");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic microcirculation represents the capillary network of all end-organs in the body with the exception of the lung. It receives oxygenated blood from the systemic arteries and returns de-oxygenated blood to the systemic veins.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyleftoxy.jpg")));
        flow.setText("Blood Flow");
    }                                        

    private void periphartery2MouseEntered(java.awt.event.MouseEvent evt) {                                           
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pcapacitor.gif")));
        desctitle.setText("Systemic Arteries");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic arteries carry oxygenated blood from the left heart to the systemic microcirculation.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/oxyleft.jpg")));
        flow.setText("Blood Flow");
    }                                          

    private void periphartey1MouseEntered(java.awt.event.MouseEvent evt) {                                          
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pcapacitor.gif")));
        desctitle.setText("Systemic Arteries");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic arteries carry oxygenated blood from the left heart to the systemic microcirculation.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/oxyleft.jpg")));
        flow.setText("Blood Flow");
    }                                         

    private void periphviens3MouseEntered(java.awt.event.MouseEvent evt) {                                          
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pcapres.gif")));
        desctitle.setText("Systemic Veins");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic veins return de-oxygenated blood to the right heart.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyleft.jpg")));
        flow.setText("Blood Flow");
    }                                         

    private void periphveins2MouseEntered(java.awt.event.MouseEvent evt) {                                          
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pcapres.gif")));
        desctitle.setText("Systemic Veins");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic veins return de-oxygenated blood to the right heart.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyleft.jpg")));
        flow.setText("Blood Flow");
    }                                         

    private void periphveinsMouseEntered(java.awt.event.MouseEvent evt) {                                         
        circuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/pcapres.gif")));
        desctitle.setText("Systemic Veins");
        circtitle.setText("Circuit Representation");
        desc.setText("The systemic veins return de-oxygenated blood to the right heart.");
        flowleft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images6C/circuits/deoxyleft.jpg")));
        flow.setText("Blood Flow");
    }                                        

}
