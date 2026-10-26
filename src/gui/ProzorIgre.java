package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logika.Igra;
import logika.Polje;
import logika.StanjePolja;
import logika.StatusIgre;

public class ProzorIgre extends JFrame{

	private Igra igra;
	
	private JPanel gornjiPanel;
	private JLabel statusLabel;
	private JLabel mineLabel;
	private JButton novaIgraBtn;
	
	private JPanel gridPanel;
	private JButton[][] dugmad;
	
	private int R, C, M;
	
	public ProzorIgre() {
		super("Minesweeper");
		
		igra = new Igra();
		R = 9; C = 9; M = 10;
		igra.resetuj(R, C, M);
		
		setLayout(new GridBagLayout());
		
		dodajGornjiPanel();
		dodajGridPanel();
		osvjeziPrikaz();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setSize(700, 700);
		setLocationRelativeTo(null);
	}
	
	private void dodajGornjiPanel() {
		gornjiPanel = new JPanel(new GridLayout(1, 3));
		gornjiPanel.setBackground(new Color(220, 245, 220));
		
		statusLabel = new JLabel();
		
		mineLabel = new JLabel();
		mineLabel.setBackground(Color.white);
		
		novaIgraBtn = napraviDugmeZaNovuIgru();
		
		gornjiPanel.add(statusLabel);
		gornjiPanel.add(mineLabel);
		gornjiPanel.add(novaIgraBtn);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty  = 0.3;
		c.insets = new Insets(5, 5, 5, 5);
		this.add(gornjiPanel, c);
	}
	private JButton napraviDugmeZaNovuIgru() {
		JButton btn = new JButton("Nova igra");
		btn.setFont(new Font("Arial", Font.BOLD, 12));
		btn.setBackground(new Color(100, 200, 100));
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

			    String[] opcije = {
			        "Easy (9×9, 10)",
			        "Medium (16×16, 40)",
			        "Hard (16×30, 99)",
			        "Custom"
			    };

			    int izbor = JOptionPane.showOptionDialog(
			        ProzorIgre.this,
			        "Izaberi težinu igre:",
			        "Nova igra",
			        JOptionPane.DEFAULT_OPTION,
			        JOptionPane.QUESTION_MESSAGE,
			        null,
			        opcije,
			        opcije[0]
			    );

			    if (izbor == -1) return; 

			    try {
			        if (izbor == 0) {         
			            R = 9; C = 9; M = 10;
			        } else if (izbor == 1) {   
			            R = 16; C = 16; M = 40;
			        } else if (izbor == 2) {   
			            R = 16; C = 30; M = 99;
			        } else {                   
			            JTextField tfR = new JTextField();
			            JTextField tfC = new JTextField();
			            JTextField tfM = new JTextField();

			            Object[] poruka = {
			                "Redovi (R):", tfR,
			                "Kolone (C):", tfC,
			                "Mine (M):", tfM
			            };

			            int ok = JOptionPane.showConfirmDialog(
			                ProzorIgre.this,
			                poruka,
			                "Custom postavke",
			                JOptionPane.OK_CANCEL_OPTION
			            );

			            if (ok != JOptionPane.OK_OPTION) return;

			            R = Integer.parseInt(tfR.getText().trim());
			            C = Integer.parseInt(tfC.getText().trim());
			            M = Integer.parseInt(tfM.getText().trim());
			        }

			        igra.resetuj(R, C, M);

			        gridPanel.removeAll();
			        gridPanel.setLayout(new GridLayout(R, C));
			        dodajGridDugmad();

			      
			        osvjeziPrikaz();
			        gridPanel.revalidate();
			        gridPanel.repaint();

			    } catch (NumberFormatException ex) {
			        JOptionPane.showMessageDialog(
			            ProzorIgre.this,
			            "Unos mora biti broj!",
			            "Greška",
			            JOptionPane.ERROR_MESSAGE
			        );
			    } catch (IllegalArgumentException ex) {
			        JOptionPane.showMessageDialog(
			            ProzorIgre.this,
			            ex.getMessage(),
			            "Neispravne vrijednosti",
			            JOptionPane.ERROR_MESSAGE
			        );
			    }
			}

		});
		return btn;
	}

	private void dodajGridPanel() {
		gridPanel = new JPanel(new GridLayout(R, C));
		gridPanel.setBackground(Color.DARK_GRAY);
		
		
		dodajGridDugmad();
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(gridPanel, c);
	}
	private void dodajGridDugmad() {
		dugmad = new JButton[R][C];
		
		for(int i = 0; i < R ; i++) {
			for(int j = 0; j < C; j++) {
				JButton btn = new JButton();
				
				btn.setPreferredSize(new Dimension(40, 40));
				btn.setMargin(new Insets(0, 0, 0, 0));
				btn.setFont(new Font("Arial", Font.BOLD, 20));
				
				final int r = i;
				final int c = j;
				
				btn.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						if(SwingUtilities.isMiddleMouseButton(e)) {
							igra.chordKlik(r, c);
						}else if(SwingUtilities.isLeftMouseButton(e)) {
							igra.otvori(r, c);
						}else if(SwingUtilities.isRightMouseButton(e)) {
							igra.oznaci(r, c);
						}
						osvjeziPrikaz();
					}
				});
				dugmad[i][j] = btn;
				
				gridPanel.add(btn);
			}
		}
	}
	private void osvjeziPrikaz() {
		mineLabel.setText("Mine: " + igra.preostaleMine());
		mineLabel.setFont(new Font("Arial", Font.BOLD, 16));
		mineLabel.setOpaque(true);
		
		statusLabel.setText("Status: " + igra.dajStatus());
		statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
		statusLabel.setOpaque(true);
		
		if(igra.dajStatus() == StatusIgre.POBJEDA) {
			statusLabel.setBackground(new Color(144, 238, 144));
		}else if(igra.dajStatus() == StatusIgre.PORAZ) {
			statusLabel.setBackground(new Color(255, 182, 193));
		}else {
			statusLabel.setBackground(Color.white);
		}
		
		boolean otkrijMine = (igra.dajStatus() == StatusIgre.PORAZ);
		for(int r = 0; r < R ; r++) {
			for(int c = 0; c < C ; c++) {
				Polje polje = igra.dajPlocu().dajPolje(r, c);
				JButton btn = dugmad[r][c];
				
				if(polje.dajStanje() == StanjePolja.ZATVORENO) {
					btn.setText(" ");
					btn.setEnabled(true);
				}else if(polje.dajStanje() == StanjePolja.OZNACENO) {
					btn.setText("F");
					btn.setEnabled(true);
				}else {
					btn.setEnabled(false);
					if(polje.imaMinu()) {
						btn.setText("*");
					}else {
						int broj = polje.dajBrojMinaOkolo();
						if(broj == 0) 
							btn.setText("");
						else 
							btn.setText(String.valueOf(broj));	
					}
				}
				if(otkrijMine && polje.imaMinu())
					btn.setText("*");
				
			}
		}
		
	}
	

	public void prikaziProzor() {
		this.setVisible(true);
	}
	
	
	
	
	
}
