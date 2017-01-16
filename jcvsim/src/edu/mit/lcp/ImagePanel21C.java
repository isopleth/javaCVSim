package edu.mit.lcp;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;

public class ImagePanel21C extends ImagePanel {

    private final JPanel AnatomicalPanel;
    private final JLabel AscendingA;
    private final JLabel AscendingAb;
    private final JLabel AscendingAc;
    private final JLabel Brachioceph;
    private final JLabel Brachiocepha;
    private final JLabel CircuitOverview;
    private final JPanel CircuitPanel;
    private final JLabel IVCa;
    private final JLabel IVCb;
    private final JTabbedPane IllustrationTabs;
    private final JLabel LBMicro;
    private final JLabel LBVeins;
    private final JLabel LBarteries;
    private final JLabel LTHeart;
    private final JLabel LTHeartb;
    private final JLabel PulMcirc;
    private final JLabel Pularterya;
    private final JLabel Pularteryc;
    private final JLabel Pularteryd;
    private final JLabel Pulmarteryb;
    private final JLabel Pulveinb;
    private final JLabel Pulveinsa;
    private final JLabel RTHeart;
    private final JLabel RenalMicroa;
    private final JLabel RenalMicrob;
    private final JLabel SVC;
    private final JLabel SVCb;
    private final JLabel SVCc;
    private final JLabel SVCd;
    private final JLabel SplanchnicArteries;
    private final JLabel SplanchnicMicroa;
    private final JLabel SplanchnicMicrob;
    private final JLabel ThoracicAorta;
    private final JLabel ThoracicAortab;
    private final JLabel UBArtery;
    private final JLabel UBMicro;
    private final JLabel UBVeins;
    private final JLabel abdomaorta;
    private final JLabel abdomveins;
    private final JLabel circtitle;
    private final JLabel circuitimage;
    private final JTextArea descarea;
    private final JScrollPane descpane;
    private final JLabel desctitle;
    private final JLabel flow;
    private final JLabel jLabel13;
    private final JLabel jLabel21;
    private final JLabel jLabel28;
    private final JLabel jLabel3;
    private final JLabel jLabel40;
    private final JLabel jLabel41;
    private final JLabel jLabel43;
    private final JLabel jLabel44;
    private final JLabel jLabel5;
    private final JLabel jLabel8;
    private final JLabel renalVeins;
    private final JLabel renalarteries;
    private final JLabel splanchnicVeins;

    public ImagePanel21C() {
        IllustrationTabs = new JTabbedPane();
        AnatomicalPanel = new JPanel();
        UBMicro = new JLabel();
        UBVeins = new JLabel();
        jLabel3 = new JLabel();
        UBArtery = new JLabel();
        SVC = new JLabel();
        Pularteryd = new JLabel();
        PulMcirc = new JLabel();
        jLabel8 = new JLabel();
        Pulveinsa = new JLabel();
        Brachioceph = new JLabel();
        Pularteryc = new JLabel();
        jLabel13 = new JLabel();
        Pulmarteryb = new JLabel();
        AscendingAb = new JLabel();
        Pulveinb = new JLabel();
        AscendingAc = new JLabel();
        Brachiocepha = new JLabel();
        ThoracicAortab = new JLabel();
        IVCb = new JLabel();
        jLabel21 = new JLabel();
        SVCc = new JLabel();
        Pularterya = new JLabel();
        AscendingA = new JLabel();
        LTHeartb = new JLabel();
        RTHeart = new JLabel();
        LTHeart = new JLabel();
        jLabel28 = new JLabel();
        ThoracicAorta = new JLabel();
        abdomveins = new JLabel();
        splanchnicVeins = new JLabel();
        renalVeins = new JLabel();
        SplanchnicMicroa = new JLabel();
        SplanchnicMicrob = new JLabel();
        RenalMicroa = new JLabel();
        SplanchnicArteries = new JLabel();
        LBVeins = new JLabel();
        jLabel40 = new JLabel();
        RenalMicrob = new JLabel();
        jLabel41 = new JLabel();
        renalarteries = new JLabel();
        abdomaorta = new JLabel();
        jLabel43 = new JLabel();
        jLabel44 = new JLabel();
        LBarteries = new JLabel();
        LBMicro = new JLabel();
        descpane = new JScrollPane();
        descarea = new JTextArea();
        desctitle = new JLabel();
        circtitle = new JLabel();
        circuitimage = new JLabel();
        SVCd = new JLabel();
        IVCa = new JLabel();
        SVCb = new JLabel();
        jLabel5 = new JLabel();
        flow = new JLabel();
        CircuitPanel = new JPanel();
        CircuitOverview = new JLabel();

        AnatomicalPanel.setBackground(new Color(255, 255, 255));
        UBMicro.setIcon(new ImageIcon(getClass().getResource("/images21C/uppermicro.jpg")));
        UBMicro.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Upper Body Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    UBMicroMouseEntered(evt);
		    highlightParameterTable("Upper Body Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    UBMicroMouseExited(evt);
		}
	    });
	
        UBVeins.setIcon(new ImageIcon(getClass().getResource("/images21C/uppveins.jpg")));
        UBVeins.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Upper Body Veins");
		}            
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    UBVeinsMouseEntered(evt);
		    highlightParameterTable("Upper Body Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    UBVeinsMouseExited(evt);
		}
	    });

        jLabel3.setIcon(new ImageIcon(getClass().getResource("/images21C/uppspace.jpg")));

        UBArtery.setIcon(new ImageIcon(getClass().getResource("/images21C/upperarteries.jpg")));
        UBArtery.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Upper Body Arteries");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    UBArteryMouseEntered(evt);
		    highlightParameterTable("Upper Body Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    UBArteryMouseExited(evt);
		}
	    });

        SVC.setIcon(new ImageIcon(getClass().getResource("/images21C/superiorVC.jpg")));
        SVC.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Superior Vena Cava");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SVCMouseEntered(evt);
		    highlightParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SVCMouseExited(evt);
		}
	    });

        Pularteryd.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmarteryA.jpg")));
        Pularteryd.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PularterydMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PularterydMouseExited(evt);
		}
	    });

        PulMcirc.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmicro.jpg")));
        PulMcirc.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Microcirculation");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PulMcircMouseEntered(evt);
		    highlightParameterTable("Pulmonary Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PulMcircMouseExited(evt);
		}
	    });
	
        jLabel8.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmspace.jpg")));

        Pulveinsa.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmvein.jpg")));
        Pulveinsa.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Veins");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PulveinsaMouseEntered(evt);
		    highlightParameterTable("Pulmonary Veins");
		}
                @Override
 		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PulveinsaMouseExited(evt);
	 	}
	    });

        Brachioceph.setIcon(new ImageIcon(getClass().getResource("/images21C/brachiocephalicA.jpg")));
        Brachioceph.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Brachiocephalic Arteries");
		}      
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    BrachiocephMouseEntered(evt);
		    highlightParameterTable("Brachiocephalic Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    BrachiocephMouseExited(evt);
		}
	    });
	
        Pularteryc.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmarteryB.jpg")));
        Pularteryc.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PularterycMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
 		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
	 	    PularterycMouseExited(evt);
		}
	    });

        jLabel13.setIcon(new ImageIcon(getClass().getResource("/images21C/leftheartA.jpg")));
        jLabel13.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Left Heart");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    jLabel13MouseEntered(evt);
		    highlightParameterTable("Left Heart");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    jLabel13MouseExited(evt);
		}
	    });

        Pulmarteryb.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmarteryC.jpg")));
        Pulmarteryb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PulmarterybMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PulmarterybMouseExited(evt);
		}
	    });

        AscendingAb.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaorta.jpg")));
        AscendingAb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Ascending Aorta");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    AscendingAbMouseEntered(evt);
		    highlightParameterTable("Ascending Aorta");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    AscendingAbMouseExited(evt);
		}
	    });

        Pulveinb.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmveinB.jpg")));
        Pulveinb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Veins");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PulveinbMouseEntered(evt);
		    highlightParameterTable("Pulmonary Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PulveinbMouseExited(evt);
		}
	    });

        AscendingAc.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaortaB.jpg")));
        AscendingAc.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Ascending Aorta");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    AscendingAcMouseEntered(evt);
		    filterParameterTable("Ascending Aorta");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    AscendingAcMouseExited(evt);
		}
	    });

        Brachiocepha.setIcon(new ImageIcon(getClass().getResource("/images21C/brachiocephalicB.jpg")));
        Brachiocepha.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Brachiocephalic Arteries");
		} 	
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    BrachiocephaMouseEntered(evt);
		    highlightParameterTable("Brachiocephalic Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    BrachiocephaMouseExited(evt);
		}
	    });

        ThoracicAortab.setIcon(new ImageIcon(getClass().getResource("/images21C/thoracicaortaA.jpg")));
        ThoracicAortab.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Thoracic Aorta");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    ThoracicAortabMouseEntered(evt);
		    highlightParameterTable("Thoracic Aorta");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    ThoracicAortabMouseExited(evt);
		}
	    });

        IVCb.setIcon(new ImageIcon(getClass().getResource("/images21C/inferiorVCB.jpg")));
        IVCb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Inferior Vena Cava");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    IVCbMouseEntered(evt);
		    highlightParameterTable("Inferior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    IVCbMouseExited(evt);
		}
	    });

        jLabel21.setIcon(new ImageIcon(getClass().getResource("/images21C/leftheartspaceA.jpg")));

        SVCc.setIcon(new ImageIcon(getClass().getResource("/images21C/leftheartB.jpg")));
        SVCc.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Superior Vena Cava");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SVCcMouseEntered(evt);
		    highlightParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SVCcMouseExited(evt);
		}
	    });

        Pularterya.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmarteryD.jpg")));
        Pularterya.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    PularteryaMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    PularteryaMouseExited(evt);		    
		}
	    });

        AscendingA.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaortaC.jpg")));
        AscendingA.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Pulmonary Arteries");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    AscendingAMouseEntered(evt);
		    highlightParameterTable("Pulmonary Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    AscendingAMouseExited(evt);
		}
	    });

        LTHeartb.setIcon(new ImageIcon(getClass().getResource("/images21C/rightheartA.jpg")));
        LTHeartb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Left Heart");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    LTHeartbMouseEntered(evt);
		    highlightParameterTable("Left Heart");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    LTHeartbMouseExited(evt);
		}
	    });

        RTHeart.setIcon(new ImageIcon(getClass().getResource("/images21C/leftheartC.jpg")));
        RTHeart.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Right Heart");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    RTHeartMouseEntered(evt);
		    highlightParameterTable("Right Heart");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    RTHeartMouseExited(evt);
		}
	    });

        LTHeart.setIcon(new ImageIcon(getClass().getResource("/images21C/rightheartB.jpg")));
        LTHeart.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Left Heart");
		} 
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    LTHeartMouseEntered(evt);
		    highlightParameterTable("Left Heart");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    LTHeartMouseExited(evt);
		}
	    });

        jLabel28.setIcon(new ImageIcon(getClass().getResource("/images21C/heartspace.jpg")));

        ThoracicAorta.setIcon(new ImageIcon(getClass().getResource("/images21C/thoracicaortaB.jpg")));
        ThoracicAorta.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Thoracic Aorta");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    ThoracicAortaMouseEntered(evt);
		    highlightParameterTable("Thoracic Aorta");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    ThoracicAortaMouseExited(evt);
		}
	    });

        abdomveins.setIcon(new ImageIcon(getClass().getResource("/images21C/abdominalveins.jpg")));
        abdomveins.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Abdominal Veins");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    abdomveinsMouseEntered(evt);
		    highlightParameterTable("Abdominal Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    abdomveinsMouseExited(evt);
		}
	    });
	
        splanchnicVeins.setIcon(new ImageIcon(getClass().getResource("/images21C/splanchnicveins.jpg")));
        splanchnicVeins.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Splanchnic Veins");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    splanchnicVeinsMouseEntered(evt);
		    highlightParameterTable("Splanchnic Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    splanchnicVeinsMouseExited(evt);
		}
	    });

        renalVeins.setIcon(new ImageIcon(getClass().getResource("/images21C/renalveins.jpg")));
        renalVeins.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Renal Veins");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    renalVeinsMouseEntered(evt);
		    highlightParameterTable("Renal Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    renalVeinsMouseExited(evt);
		}
	    });

        SplanchnicMicroa.setIcon(new ImageIcon(getClass().getResource("/images21C/spalchnicmicro.jpg")));
        SplanchnicMicroa.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Splanchnic Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SplanchnicMicroaMouseEntered(evt);
		    highlightParameterTable("Splanchnic Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SplanchnicMicroaMouseExited(evt);
		}
	    });

        SplanchnicMicrob.setIcon(new ImageIcon(getClass().getResource("/images21C/spalchnicmicroB.jpg")));
        SplanchnicMicrob.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Splanchnic Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SplanchnicMicrobMouseEntered(evt);
		    highlightParameterTable("Splanchnic Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SplanchnicMicrobMouseExited(evt);
		}
	    });

        RenalMicroa.setIcon(new ImageIcon(getClass().getResource("/images21C/renalmicro.jpg")));
        RenalMicroa.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Renal Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    RenalMicroaMouseEntered(evt);
		    highlightParameterTable("Renal Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    RenalMicroaMouseExited(evt);
		}
	    });

        SplanchnicArteries.setIcon(new ImageIcon(getClass().getResource("/images21C/splanchnicartery.jpg")));
        SplanchnicArteries.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Splanchnic Arteries");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SplanchnicArteriesMouseEntered(evt);
		    highlightParameterTable("Splanchnic Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SplanchnicArteriesMouseExited(evt);
		}
	    });

        LBVeins.setIcon(new ImageIcon(getClass().getResource("/images21C/lowerbodyveins.jpg")));
        LBVeins.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Lower Body Veins");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    LBVeinsMouseEntered(evt);
		    highlightParameterTable("Lower Body Veins");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    LBVeinsMouseExited(evt);
		}
	    });

        jLabel40.setIcon(new ImageIcon(getClass().getResource("/images21C/lbspabceA.jpg")));

        RenalMicrob.setIcon(new ImageIcon(getClass().getResource("/images21C/renalmicroB.jpg")));
        RenalMicrob.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Renal Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    RenalMicrobMouseEntered(evt);
		    highlightParameterTable("Renal Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    RenalMicrobMouseExited(evt);
		}
	    });

        jLabel41.setIcon(new ImageIcon(getClass().getResource("/images21C/lbspabceB.jpg")));

        renalarteries.setIcon(new ImageIcon(getClass().getResource("/images21C/renalartery.jpg")));
        renalarteries.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Renal Arteries");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    renalarteriesMouseEntered(evt);
		    highlightParameterTable("Renal Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    renalarteriesMouseExited(evt);
		}
	    });

        abdomaorta.setIcon(new ImageIcon(getClass().getResource("/images21C/abdominalaorta.jpg")));
        abdomaorta.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Abdominal Aorta");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    abdomaortaMouseEntered(evt);
		    highlightParameterTable("Abdominal Aorta");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    abdomaortaMouseExited(evt);
		}
	    });

        jLabel43.setIcon(new ImageIcon(getClass().getResource("/images21C/lbspabceC.jpg")));

        jLabel44.setIcon(new ImageIcon(getClass().getResource("/images21C/lbspabceD.jpg")));

        LBarteries.setIcon(new ImageIcon(getClass().getResource("/images21C/lowerbodyartery.jpg")));
        LBarteries.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Lower Body Arteries");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    LBarteriesMouseEntered(evt);
		    highlightParameterTable("Lower Body Arteries");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    LBarteriesMouseExited(evt);
		}
	    });

        LBMicro.setIcon(new ImageIcon(getClass().getResource("/images21C/lowerbodymicro.jpg")));
        LBMicro.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Lower Body Microcirculation");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    LBMicroMouseEntered(evt);
		    highlightParameterTable("Lower Body Microcirculation");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    LBMicroMouseExited(evt);
		}
	    });

        descpane.setBorder(null);
        descarea.setColumns(20);
        descarea.setLineWrap(true);
        descarea.setRows(5);
        descarea.setWrapStyleWord(true);
        descarea.setBorder(null);
        descpane.setViewportView(descarea);

        SVCd.setIcon(new ImageIcon(getClass().getResource("/images21C/SVCb.jpg")));
        SVCd.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SVCdMouseEntered(evt);
		    highlightParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SVCdMouseExited(evt);
		}
	    });

        IVCa.setIcon(new ImageIcon(getClass().getResource("/images21C/IVCa.jpg")));
        IVCa.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Inferior Vena Cava");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    IVCaMouseEntered(evt);
		    highlightParameterTable("Inferior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    IVCaMouseExited(evt);
		}
	    });

        SVCb.setIcon(new ImageIcon(getClass().getResource("/images21C/SVCa.jpg")));
        SVCb.addMouseListener(new MouseAdapter() {
                @Override
		public void mouseClicked(MouseEvent evt) {
		    filterParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseEntered(MouseEvent evt) {
		    circtitle.setText("Circuit Representation");
		    flow.setText("Blood Flow");
		    SVCbMouseEntered(evt);
		    highlightParameterTable("Superior Vena Cava");
		}
                @Override
		public void mouseExited(MouseEvent evt) {
		    circtitle.setText(null);
		    desctitle.setText(null);
		    flow.setText(null);
		    SVCbMouseExited(evt);
		}
	    });
	
       GroupLayout AnatomicalPanelLayout = new GroupLayout(AnatomicalPanel);
        AnatomicalPanel.setLayout(AnatomicalPanelLayout);
        AnatomicalPanelLayout.setHorizontalGroup(
            AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(LBMicro)
                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(UBVeins)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel3)
                                .addGap(0, 0, 0)
                                .addComponent(UBArtery))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(IVCb)
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel21)
                                                .addGap(0, 0, 0)
                                                .addComponent(SVCc)
                                                .addGap(0, 0, 0)
                                                .addComponent(Pularterya)
                                                .addGap(0, 0, 0)
                                                .addComponent(AscendingA)
                                                .addGap(0, 0, 0)
                                                .addComponent(LTHeartb))
                                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                                .addComponent(RTHeart)
                                                .addGap(0, 0, 0)
                                                .addComponent(LTHeart)))
                                        .addGap(0, 0, 0)
                                        .addComponent(ThoracicAorta))
                                    .addComponent(jLabel28)))
                            .addComponent(UBMicro)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(abdomveins)
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(splanchnicVeins)
                                        .addGap(0, 0, 0)
                                        .addComponent(SplanchnicMicroa)
                                        .addGap(0, 0, 0)
                                        .addComponent(SplanchnicArteries)
                                        .addGap(0, 0, 0)
                                        .addComponent(abdomaorta))
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(renalVeins)
                                        .addGap(0, 0, 0)
                                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(SplanchnicMicrob)
                                            .addComponent(RenalMicroa))
                                        .addGap(0, 0, 0)
                                        .addComponent(renalarteries))))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(SVCd)
                                        .addComponent(SVC))
                                    .addComponent(IVCa))
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(Pularteryd)
                                        .addGap(0, 0, 0)
                                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                                .addComponent(PulMcirc)
                                                .addGap(0, 0, 0)
                                                .addComponent(Pulveinsa)
                                                .addGap(0, 0, 0)
                                                .addComponent(Brachioceph))
                                            .addComponent(jLabel8)))
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(SVCb)
                                        .addGap(0, 0, 0)
                                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(Pularteryc)
                                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addGap(0, 0, 0)
                                                .addComponent(Pulmarteryb)))
                                        .addGap(0, 0, 0)
                                        .addComponent(AscendingAb)
                                        .addGap(0, 0, 0)
                                        .addComponent(Pulveinb)
                                        .addGap(0, 0, 0)
                                        .addComponent(AscendingAc)
                                        .addGap(0, 0, 0)
                                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, 0)
                                                .addComponent(ThoracicAortab))
                                            .addComponent(Brachiocepha)))))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(LBVeins)
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addGap(0, 0, 0)
                                        .addComponent(RenalMicrob)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel43)
                                        .addGap(0, 0, 0)
                                        .addComponent(LBarteries))
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel44)))))
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(flow))
                                    .addComponent(jLabel5)))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(descpane, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(circuitimage)
                                    .addComponent(circtitle)
                                    .addComponent(desctitle))))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        AnatomicalPanelLayout.setVerticalGroup(
            AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(UBMicro)
                        .addGap(0, 0, 0)
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(UBVeins)
                            .addComponent(jLabel3)
                            .addComponent(UBArtery))
                        .addGap(0, 0, 0)
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(SVC)
                            .addComponent(Pularteryd)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(PulMcirc)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel8))
                            .addComponent(Pulveinsa)
                            .addComponent(Brachioceph))
                        .addGap(0, 0, 0)
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(Pularteryc)
                                .addGap(0, 0, 0)
				      .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(Pulmarteryb)))
                            .addComponent(AscendingAb)
                            .addComponent(Pulveinb)
                            .addComponent(AscendingAc)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(Brachiocepha)
                                .addGap(0, 0, 0)
                                .addComponent(ThoracicAortab))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(SVCd)
                                .addGap(0, 0, 0)
                                .addComponent(IVCa))
                            .addComponent(SVCb))
                        .addGap(0, 0, 0)
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(IVCb)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(SVCc)
                                    .addComponent(Pularterya))
                                .addGap(0, 0, 0)
                                .addComponent(RTHeart)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel28))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(AscendingA)
                                    .addComponent(LTHeartb))
                                .addGap(0, 0, 0)
                                .addComponent(LTHeart))
                            .addComponent(ThoracicAorta)))
                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(desctitle)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descpane, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)))
                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(abdomveins)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(splanchnicVeins)
                                .addGap(0, 0, 0)
                                .addComponent(renalVeins))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(SplanchnicMicroa)
                                .addGap(0, 0, 0)
                                .addComponent(SplanchnicMicrob)
                                .addGap(0, 0, 0)
                                .addComponent(RenalMicroa))
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addComponent(SplanchnicArteries)
                                .addGap(0, 0, 0)
                                .addComponent(renalarteries))
                            .addComponent(abdomaorta))
                        .addGap(0, 0, 0)
                        .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(LBVeins)
                            .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, 0)
                                        .addComponent(RenalMicrob))
                                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel43)))
                                .addGap(0, 0, 0)
                                .addGroup(AnatomicalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel44)))
                            .addComponent(LBarteries))
                        .addGap(0, 0, 0)
                        .addComponent(LBMicro))
                    .addGroup(AnatomicalPanelLayout.createSequentialGroup()
                        .addComponent(circtitle)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(circuitimage)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flow)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        IllustrationTabs.addTab("Anatomical Overview", AnatomicalPanel);

        CircuitPanel.setBackground(new Color(255, 255, 255));
        CircuitOverview.setIcon(new ImageIcon(getClass().getResource("/images21C/Circuit.gif")));

        GroupLayout CircuitPanelLayout = new GroupLayout(CircuitPanel);
        CircuitPanel.setLayout(CircuitPanelLayout);
        CircuitPanelLayout.setHorizontalGroup(
            CircuitPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(CircuitPanelLayout.createSequentialGroup()
                .addComponent(CircuitOverview)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CircuitPanelLayout.setVerticalGroup(
            CircuitPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(CircuitPanelLayout.createSequentialGroup()
                .addComponent(CircuitOverview)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        IllustrationTabs.addTab("Circuit Overview", CircuitPanel);

        GroupLayout layout = new GroupLayout(this);
	layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(IllustrationTabs, GroupLayout.PREFERRED_SIZE, 508, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(IllustrationTabs, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );

    } // end constructor
 

    private void PularterycMouseExited(MouseEvent evt) {//GEN-FIRST:event_PularterycMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PularterycMouseExited

    private void BrachiocephaMouseExited(MouseEvent evt) {//GEN-FIRST:event_BrachiocephaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_BrachiocephaMouseExited

    private void ThoracicAortabMouseExited(MouseEvent evt) {//GEN-FIRST:event_ThoracicAortabMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_ThoracicAortabMouseExited

    private void SVCcMouseExited(MouseEvent evt) {//GEN-FIRST:event_SVCcMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SVCcMouseExited

    private void PulveinbMouseExited(MouseEvent evt) {//GEN-FIRST:event_PulveinbMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PulveinbMouseExited

    private void PulveinsaMouseExited(MouseEvent evt) {//GEN-FIRST:event_PulveinsaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PulveinsaMouseExited

    private void PulMcircMouseExited(MouseEvent evt) {//GEN-FIRST:event_PulMcircMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PulMcircMouseExited

    private void PularterydMouseExited(MouseEvent evt) {//GEN-FIRST:event_PularterydMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PularterydMouseExited

    private void PularteryaMouseExited(MouseEvent evt) {//GEN-FIRST:event_PularteryaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PularteryaMouseExited

    private void PulmarterybMouseExited(MouseEvent evt) {//GEN-FIRST:event_PulmarterybMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_PulmarterybMouseExited

    private void BrachiocephaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_BrachiocephaMouseEntered
        desctitle.setText("Brachiocephalic Arteries");
        descarea.setText("These vessels supply oxygenated blood to the upper body arteries.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/3.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_BrachiocephaMouseEntered

    private void SVCcMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SVCcMouseEntered
        desctitle.setText("Superior Vena Cava");
        descarea.setText("This intra-thoracic vein carries de-oxygenated blood from the upper body veins to the right atrium.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/5.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_SVCcMouseEntered

    private void ThoracicAortabMouseEntered(MouseEvent evt) {//GEN-FIRST:event_ThoracicAortabMouseEntered
        desctitle.setText("Thoracic Aorta:");
        descarea.setText("This section of the aorta is within the thorax and serves as a conduit of blood to the arteries of the viscera and lower extremities.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/6.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_ThoracicAortabMouseEntered

    private void PulveinbMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PulveinbMouseEntered
        desctitle.setText("Pulmonary Vein:");
        descarea.setText("The pulmonary veins deliver oxygenated blood to the left heart.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryveins.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_PulveinbMouseEntered

    private void PulveinsaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PulveinsaMouseEntered
        desctitle.setText("Pulmonary Vein:");
        descarea.setText("The pulmonary veins deliver oxygenated blood to the left heart.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryveins.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_PulveinsaMouseEntered

    private void PulMcircMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PulMcircMouseEntered
        desctitle.setText("Pulmonary Microcirculation:");
        descarea.setText("In the pulmonary microcirculation oxygen exchange takes place across the membranes separating pulmonary alveoli from the pulmonary capillary network. The pulmonary microcirculation thus receives de-oxygenated blood from the pulmonary arteries and returns oxygenated blood to the pulmonary veins.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyrightde.jpg")));
    }//GEN-LAST:event_PulMcircMouseEntered

    private void PularterydMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PularterydMouseEntered
        desctitle.setText("Pulmonary Artery:");
        descarea.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryartery.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_PularterydMouseEntered

    private void PularterycMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PularterycMouseEntered
        desctitle.setText("Pulmonary Artery:");
        descarea.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryartery.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_PularterycMouseEntered

    private void PulmarterybMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PulmarterybMouseEntered
        desctitle.setText("Pulmonary Artery:");
        descarea.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryartery.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_PulmarterybMouseEntered

    private void PularteryaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_PularteryaMouseEntered
        desctitle.setText("Pulmonary Artery:");
        descarea.setText("The pulmonary arteries carry de-oxygenated blood from the right heart to the pulmonary microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/pulmonaryartery.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_PularteryaMouseEntered

    private void jLabel13MouseExited(MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseExited
        resetUIfunctions();
    }//GEN-LAST:event_jLabel13MouseExited

    private void jLabel13MouseEntered(MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseEntered
        desctitle.setText("Superior Vena Cava");
        descarea.setText("The systemic upper body arteries carry oxygenated blood from the left heart to the upper body microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/5.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_jLabel13MouseEntered

    private void AscendingAMouseExited(MouseEvent evt) {//GEN-FIRST:event_AscendingAMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_AscendingAMouseExited

    private void AscendingAMouseEntered(MouseEvent evt) {//GEN-FIRST:event_AscendingAMouseEntered
        desctitle.setText("Ascending Aorta:");
        descarea.setText("The ascending aorta receives oxygenated blood directly from the left ventricle and delivers it to the major branches of the systemic arterial system.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaorta.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_AscendingAMouseEntered

    private void SVCbMouseExited(MouseEvent evt) {//GEN-FIRST:event_SVCbMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SVCbMouseExited

    private void SVCbMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SVCbMouseEntered
        desctitle.setText("Superior Vena Cava");
        descarea.setText("This intra-thoracic vein carries de-oxygenated blood from the upper body veins to the right atrium.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/5.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_SVCbMouseEntered

    private void SVCdMouseExited(MouseEvent evt) {//GEN-FIRST:event_SVCdMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SVCdMouseExited

    private void SVCdMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SVCdMouseEntered
        desctitle.setText("Superior Vena Cava");
        descarea.setText("This intra-thoracic vein carries de-oxygenated blood from the upper body veins to the right atrium.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/5.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_SVCdMouseEntered

    private void IVCaMouseExited(MouseEvent evt) {//GEN-FIRST:event_IVCaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_IVCaMouseExited

    private void IVCaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_IVCaMouseEntered
        desctitle.setText("Inferior Vena Cava:");
        descarea.setText("Delivers de-oxygenated blood from the abdominal inferior vena cava to the hearts right atrium");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/15.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_IVCaMouseEntered

    private void RenalMicrobMouseExited(MouseEvent evt) {//GEN-FIRST:event_RenalMicrobMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_RenalMicrobMouseExited

    private void RenalMicroaMouseExited(MouseEvent evt) {//GEN-FIRST:event_RenalMicroaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_RenalMicroaMouseExited

    private void SplanchnicMicrobMouseExited(MouseEvent evt) {//GEN-FIRST:event_SplanchnicMicrobMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SplanchnicMicrobMouseExited

    private void SplanchnicMicroaMouseExited(MouseEvent evt) {//GEN-FIRST:event_SplanchnicMicroaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SplanchnicMicroaMouseExited

    private void RenalMicrobMouseEntered(MouseEvent evt) {//GEN-FIRST:event_RenalMicrobMouseEntered
        desctitle.setText("Renal Microcirculation:");
        descarea.setText("The renal microcirculation represents the arterioles and capillary networks of the kidneys.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_RenalMicrobMouseEntered

    private void RenalMicroaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_RenalMicroaMouseEntered
        desctitle.setText("Renal Microcirculation:");
        descarea.setText("The renal microcirculation represents the arterioles and capillary networks of the kidneys.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_RenalMicroaMouseEntered

    private void SplanchnicMicrobMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SplanchnicMicrobMouseEntered
        desctitle.setText("Splanchnic Microcirculation:");
        descarea.setText("The splanchnic microcirculation represents the arterioles and capillary networks of the abdominal viscera. ");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_SplanchnicMicrobMouseEntered

    private void SplanchnicMicroaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SplanchnicMicroaMouseEntered
        desctitle.setText("Splanchnic Microcirculation:");
        descarea.setText("The splanchnic microcirculation represents the arterioles and capillary networks of the abdominal viscera. ");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_SplanchnicMicroaMouseEntered

    private void RTHeartMouseExited(MouseEvent evt) {//GEN-FIRST:event_RTHeartMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_RTHeartMouseExited

    private void RTHeartMouseEntered(MouseEvent evt) {//GEN-FIRST:event_RTHeartMouseEntered
        desctitle.setText("Right Heart:");
        descarea.setText("During diastole, the right heart receives de-oxygenated blood from the superior and inferior vena cavae; during systole, it generates the required pressure to propel the blood into the pulmonary arteries. The right heart is comprised of the right atrium and the right ventricle.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/heart.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_RTHeartMouseEntered

    private void LTHeartbMouseExited(MouseEvent evt) {//GEN-FIRST:event_LTHeartbMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_LTHeartbMouseExited

    private void LTHeartbMouseEntered(MouseEvent evt) {//GEN-FIRST:event_LTHeartbMouseEntered
        desctitle.setText("Left Heart:");
        descarea.setText("During diastole, the left heart receives oxygenated blood from the pulmonary veins; during systole, it generates the required pressure to propel the blood into the ascending aorta. The left heart is comprised of the left atrium and the left ventricle.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/heart.gif")));
         flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_LTHeartbMouseEntered

    private void LTHeartMouseExited(MouseEvent evt) {//GEN-FIRST:event_LTHeartMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_LTHeartMouseExited

    private void LTHeartMouseEntered(MouseEvent evt) {//GEN-FIRST:event_LTHeartMouseEntered
        desctitle.setText("Left Heart:");
        descarea.setText("During diastole, the left heart receives oxygenated blood from the pulmonary veins; during systole, it generates the required pressure to propel the blood into the ascending aorta. The left heart is comprised of the left atrium and the left ventricle.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/heart.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_LTHeartMouseEntered

    private void AscendingAcMouseExited(MouseEvent evt) {//GEN-FIRST:event_AscendingAcMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_AscendingAcMouseExited

    private void AscendingAcMouseEntered(MouseEvent evt) {//GEN-FIRST:event_AscendingAcMouseEntered
        desctitle.setText("Ascending Aorta:");
        descarea.setText("The ascending aorta receives oxygenated blood directly form the left ventricle and delivers it to the major branches of the systemic arterial system.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaorta.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_AscendingAcMouseEntered

    private void AscendingAbMouseExited(MouseEvent evt) {//GEN-FIRST:event_AscendingAbMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_AscendingAbMouseExited

    private void AscendingAbMouseEntered(MouseEvent evt) {//GEN-FIRST:event_AscendingAbMouseEntered
        desctitle.setText("Ascending Aorta:");
        descarea.setText("The ascending aorta receives oxygenated blood directly from the left ventricle and delivers it to the major branches of the systemic arterial system.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/ascendingaorta.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_AscendingAbMouseEntered

    private void splanchnicVeinsMouseExited(MouseEvent evt) {//GEN-FIRST:event_splanchnicVeinsMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_splanchnicVeinsMouseExited

    private void splanchnicVeinsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_splanchnicVeinsMouseEntered
        desctitle.setText("Splanchnic Veins:");
        descarea.setText("Returns de-oxygenated blood from the intra-abdominal viscera to the abdominal inferior vena cava.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/911.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleft.jpg")));
    }//GEN-LAST:event_splanchnicVeinsMouseEntered

    private void SplanchnicArteriesMouseExited(MouseEvent evt) {//GEN-FIRST:event_SplanchnicArteriesMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SplanchnicArteriesMouseExited

    private void SplanchnicArteriesMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SplanchnicArteriesMouseEntered
        desctitle.setText("Splanchnic Arteries:");
        descarea.setText("Branching off the abdominal aorta, the splanchnic arteries supply oxygenated blood to the internal abdominal viscera, including the GI tract, liver, spleen and pancreas.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/810.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_SplanchnicArteriesMouseEntered

    private void renalVeinsMouseExited(MouseEvent evt) {//GEN-FIRST:event_renalVeinsMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_renalVeinsMouseExited

    private void renalVeinsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_renalVeinsMouseEntered
        desctitle.setText("Renal Veins:");
        descarea.setText("Return de-oxygenated blood from the kidneys to the abdominal vein.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/911.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleft.jpg")));
    }//GEN-LAST:event_renalVeinsMouseEntered

    private void renalarteriesMouseExited(MouseEvent evt) {//GEN-FIRST:event_renalarteriesMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_renalarteriesMouseExited

    private void renalarteriesMouseEntered(MouseEvent evt) {//GEN-FIRST:event_renalarteriesMouseEntered
        desctitle.setText("Renal Artery:");
        descarea.setText("Branching off the abdominal aorta, the renal artery supplies oxygenated blood to the kidneys.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/810.jpg")));
         flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_renalarteriesMouseEntered

    private void IVCbMouseExited(MouseEvent evt) {//GEN-FIRST:event_IVCbMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_IVCbMouseExited

    private void IVCbMouseEntered(MouseEvent evt) {//GEN-FIRST:event_IVCbMouseEntered
        desctitle.setText("Inferior Vena Cava:");
        descarea.setText("Delivers de-oxygenated blood from the abdominal inferior vena cava to the hearts right atrium");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/15.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_IVCbMouseEntered

    private void ThoracicAortaMouseExited(MouseEvent evt) {//GEN-FIRST:event_ThoracicAortaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_ThoracicAortaMouseExited

    private void ThoracicAortaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_ThoracicAortaMouseEntered
        desctitle.setText("Thoracic Aorta:");
        descarea.setText("This section of the aorta is within the thorax and serves as a conduit of blood to the arteries of the viscera and lower extremities.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/6.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_ThoracicAortaMouseEntered

    private void abdomveinsMouseExited(MouseEvent evt) {//GEN-FIRST:event_abdomveinsMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_abdomveinsMouseExited

    private void abdomveinsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_abdomveinsMouseEntered
        desctitle.setText("Abdominal Veins:");
        descarea.setText("The abdominal portion of the inferior vena cava that returns de-oxygenated blood from the abdominal viscera and lower body circulation to the thoracic portion of inferior vena cava. ");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/14.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_abdomveinsMouseEntered

    private void abdomaortaMouseExited(MouseEvent evt) {//GEN-FIRST:event_abdomaortaMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_abdomaortaMouseExited

    private void abdomaortaMouseEntered(MouseEvent evt) {//GEN-FIRST:event_abdomaortaMouseEntered
        desctitle.setText("Abdominal Aorta:");
        descarea.setText("A continuation of the thoracic aorta, the abdominal aorta carries oxygenated blood to the splanchnic, renal, and lower body arterial branches.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/7.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyright.jpg")));
    }//GEN-LAST:event_abdomaortaMouseEntered

    private void SVCMouseExited(MouseEvent evt) {//GEN-FIRST:event_SVCMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_SVCMouseExited

    private void SVCMouseEntered(MouseEvent evt) {//GEN-FIRST:event_SVCMouseEntered
        desctitle.setText("Superior Vena Cava");
        descarea.setText("This intra-thoracic vein carries de-oxygenated blood from the upper body veins to the right atrium.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/5.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyright.jpg")));
    }//GEN-LAST:event_SVCMouseEntered

    private void BrachiocephMouseExited(MouseEvent evt) {//GEN-FIRST:event_BrachiocephMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_BrachiocephMouseExited

    private void BrachiocephMouseEntered(MouseEvent evt) {//GEN-FIRST:event_BrachiocephMouseEntered
        desctitle.setText("Brachiocephalic Arteries:");
        descarea.setText("These vessels supply oxygenated blood to the upper body arteries.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/2.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_BrachiocephMouseEntered

    private void LBVeinsMouseExited(MouseEvent evt) {//GEN-FIRST:event_LBVeinsMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_LBVeinsMouseExited

    private void LBVeinsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_LBVeinsMouseEntered
        desctitle.setText("Lower Body Veins:");
        descarea.setText("The lower body veins return de-oxygenated blood to the abdominal inferior vena cava.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/13.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleft.jpg")));
    }//GEN-LAST:event_LBVeinsMouseEntered

    private void LBarteriesMouseExited(MouseEvent evt) {//GEN-FIRST:event_LBarteriesMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_LBarteriesMouseExited

    private void LBarteriesMouseEntered(MouseEvent evt) {//GEN-FIRST:event_LBarteriesMouseEntered
        desctitle.setText("Lower Body Arteries:");
        descarea.setText("The lower body arteries carry oxygenated blood from the abdominal aorta to the lower body microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/12.jpg")));
         flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_LBarteriesMouseEntered

    private void LBMicroMouseExited(MouseEvent evt) {//GEN-FIRST:event_LBMicroMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_LBMicroMouseExited

    private void LBMicroMouseEntered(MouseEvent evt) {//GEN-FIRST:event_LBMicroMouseEntered
        desctitle.setText("Lower Body Microcirculation:");
        descarea.setText("The lower body microcirculation represents the arterioles and capillary network of all blood vessels in the lower body. It receives oxygenated blood from the systemic lower body arteries and returns de-oxygenated blood to the systemic lower body veins.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
         flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_LBMicroMouseEntered

    private void UBVeinsMouseExited(MouseEvent evt) {//GEN-FIRST:event_UBVeinsMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_UBVeinsMouseExited

    private void UBVeinsMouseEntered(MouseEvent evt) {//GEN-FIRST:event_UBVeinsMouseEntered
        desctitle.setText("Upper Body Veins:");
        descarea.setText("The upper body veins receive de-oxygenated blood from the upper body capillary networks and pass it to the superior vena cava.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/4.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleft.jpg")));
    }//GEN-LAST:event_UBVeinsMouseEntered

    private void resetUIfunctions(){
        descarea.setText(null);
        circuitimage.setIcon(null);
        flow.setIcon(null);
    }
    
    private void UBArteryMouseExited(MouseEvent evt) {//GEN-FIRST:event_UBArteryMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_UBArteryMouseExited

    private void UBArteryMouseEntered(MouseEvent evt) {//GEN-FIRST:event_UBArteryMouseEntered
        desctitle.setText("Upper Body Arteries:");
        descarea.setText("The upper body arteries carry oxygenated blood from the aortic branches to the upper body microcirculation.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/3.jpg")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/oxyleft.jpg")));
    }//GEN-LAST:event_UBArteryMouseEntered

    private void UBMicroMouseExited(MouseEvent evt) {//GEN-FIRST:event_UBMicroMouseExited
        resetUIfunctions();
    }//GEN-LAST:event_UBMicroMouseExited

    private void UBMicroMouseEntered(MouseEvent evt) {//GEN-FIRST:event_UBMicroMouseEntered
        desctitle.setText("Upper Body Microcirculation:");
        descarea.setText("The upper body microcirculation represents the arterioles and capillary networks of all blood vessels in the upper body. It receives oxygenated blood from the upper body arteries and returns de-oxygenated blood to the upper body veins.");
        circuitimage.setIcon(new ImageIcon(getClass().getResource("/images21C/Resistor.gif")));
        flow.setIcon(new ImageIcon(getClass().getResource("/images21C/deoxyleftoxy.jpg")));
    }//GEN-LAST:event_UBMicroMouseEntered
    
   
}
