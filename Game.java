import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;



public class Game {

	
	
	private JFrame frame;
	private static JButton Button1;
	private static JButton Button2;
	private static JButton Button3;
	private static JButton Button4;
	private static JButton Button5;
	private static JButton Button6;
	private static JButton Button7;
	private static JButton Button8;
	private static JButton Button9;
	
	
	//keeps track of results (1 is player 1, 2 is player 2).
	static int[] results = new int[9];

	boolean client1 = true;
	
	 //spaces on tic tac toe board
	static boolean one = false;
	static boolean two = false;
	static boolean three = false;
	static boolean four = false;
	static boolean five = false;
	static boolean six = false;
	static boolean seven = false;
	static boolean eight = false;
	static boolean nine = false;

	
	
	//main method
	public static void main(String[] args) {
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game window = new Game();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		
	}
	
	
	
	public static boolean winner(){
		

		
		
		if ((results[0] == results[1] && results[0]== results[2]) ||
		(results[0] == results[3] && results[0] == results[6] && results[0] != 0) ||
		(results[0] == results[4] && results[0] == results[8] && results[0] != 0) ||
		(results[1] == results[4] && results[1] == results[7] && results[1] != 0) ||
		(results[2] == results[5] && results[2] == results[8] && results[2] != 0) || 
		(results[3] == results[4] && results[3] == results[5] && results[3] != 0) ||
		(results[6] == results[7] && results[6] == results[8] && results[6] != 0) ||
		(results[2] == results[4] && results[2] == results[6] && results[2] != 0)) {
			return true;
		}
		
		return false;
	}
	
	
	
	
	public static void reset() {
		
		for(int i = 0; i < results.length; i++) {
			results[i] = 0;
		}
		
		
		one = false;
		two = false;
		three = false;
		four = false;
		five = false;
		six = false;
		seven = false;
		eight = false;
		nine = false;

		
		Button1.setText("");
		Button2.setText("");
		Button3.setText("");
		Button4.setText("");
		Button5.setText("");
		Button6.setText("");
		Button7.setText("");
		Button8.setText("");
		Button9.setText("");
	}
	
		
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public Game() {
		initialize();
		
	}
	
	
	
	private void initialize() {
		
		frame = new JFrame("Tic-Tac-Toe");
		frame.setBounds(100, 100, 900, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		
		
		Button1 = new JButton("");
		Button1.setBounds(34, 24, 127, 75);
		panel.add(Button1);
		
		Button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if((one == false) && client1 == true) {
					one = true;
					Button1.setText("X");
					client1 = false;
					results[0] = 1;
			
					
				} else if((one == false) && client1 != true) {
					
					one = true;
					Button1.setText("O");
					client1 = true;
					results[0] = 2;
					
			
				}
				
				if(winner() == true && client1 == true) {
					System.out.println("Player 1 wins");
				}else if (winner() == true && client1 == false){
					System.out.println("Player 2 wins");
				}
				
			}
		});
		
		
		Button2 = new JButton("");
		Button2.setBounds(162, 24, 127, 75);
		panel.add(Button2);
		
		Button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if((two == false) && client1 == true) {
					two = true;
					Button2.setText("X");
					client1 = false;
					results[1] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
					
					
				} else if((two == false) && client1 != true) {
					
					two = true;
					Button2.setText("O");
					client1 = true;
					results[1] = 2;
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
			}
				
			
		});
		
		
		Button3 = new JButton("");
		Button3.setBounds(290, 24, 127, 75);
		panel.add(Button3);
		
		Button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((three == false) && client1 == true) {
					three = true;
					Button3.setText("X");
					client1 = false;
					results[2] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
				} else if((three == false) && client1 != true) {
					
					three = true;
					Button3.setText("O");
					client1 = true;
					results[2] = 2;
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
			}
			
		});
		
		Button4 = new JButton("");
		Button4.setBounds(34, 97, 127, 75);
		panel.add(Button4);
		
		
		Button4.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					if((four == false) && client1 == true) {
						four = true;
						Button4.setText("X");
						client1 = false;
						results[3] = 1;
						
						if(winner() == true) {
							System.out.println("Player 1 wins");
							reset();
							
						}
						
						
					} else if((four == false) && client1 != true) {
						
						four = true;
						Button4.setText("O");
						client1 = true;
						results[3] = 2;
						
						if(winner() == true) {
							System.out.println("Player 2 wins");
							reset();
						}
					}
					
			}
		});
		
		Button5 = new JButton("");
		Button5.setBounds(162, 97, 127, 75);
		panel.add(Button5);
		
		Button5.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if((five == false) && client1 == true) {
					five = true;
					Button5.setText("X");
					client1 = false;
					results[4] = 1;
					
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
					
					
				} else if((five == false) && client1 != true) {
					
					five = true;
					Button5.setText("O");
					client1 = true;
					results[4] = 2;
					
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
					
					
				}
				
		}
	});
		
		Button6 = new JButton("");
		Button6.setBounds(290, 97, 127, 75);
		panel.add(Button6);
		
		
		Button6.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if((six == false) && client1 == true) {
					six = true;
					Button6.setText("X");
					client1 = false;
					results[5] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
					
					
				} else if((six == false) && client1 != true) {
					
					six = true;
					Button6.setText("O");
					client1 = true;
					results[5] = 2;
					
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
		}
	});
		
		
		
		Button7 = new JButton("");
		Button7.setBounds(34, 171, 127, 75);
		panel.add(Button7);
		
		
		Button7.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if((seven == false) && client1 == true) {
					seven= true;
					Button7.setText("X");
					client1 = false;
					results[6] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
				} else if((seven == false) && client1 != true) {
					
					seven = true;
					Button7.setText("O");
					client1 = true;
					results[6] = 2;
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
		}
	});
	
		
		Button8 = new JButton("");
		Button8.setBounds(162, 171, 127, 75);
		panel.add(Button8);
		
		
		
		Button8.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if((eight == false) && client1 == true) {
					eight = true;
					Button8.setText("X");
					client1 = false;
					results[7] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
					}
					
				} else if((eight == false) && client1 != true) {
					
					eight = true;
					Button8.setText("O");
					client1 = true;
					results[7] = 2;
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
		}
	});
		
		
		Button9 = new JButton("");
		Button9.setBounds(290, 171, 127, 75);
		panel.add(Button9);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(492, 24, 322, 222);
		panel.add(textArea);
		
		
		Button9.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if((nine == false) && client1 == true) {
					nine = true;
					Button9.setText("X");
					client1 = false;
					results[8] = 1;
					
					if(winner() == true) {
						System.out.println("Player 1 wins");
						reset();
						
					}
					
				} else if((nine == false) && client1 != true) {
					
					five = true;
					Button9.setText("O");
					client1 = true;
					results[8] = 2;
					
					if(winner() == true) {
						System.out.println("Player 2 wins");
						reset();
					}
				}
				
		}
	});
		
		
		
		
		
	}

	
}