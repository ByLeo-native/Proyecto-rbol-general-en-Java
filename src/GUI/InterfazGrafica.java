package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Excepciones.GInvalidOperationException;
import Excepciones.InvalidOperationException;
import Excepciones.InvalidPositionException;

public class InterfazGrafica extends JFrame {
	
	private BackgroundPrograma programa;
	private JTextField tfDisplay;
	private JPanel pnOperacion, pnNuevoNodo, pnNodoDefinido;
	private JComboBox<String> cbAction;
	private JLabel lbNuevoRotulo, lbNuevoValor, lbRotuloDeNodoDefinido, lbValorDeNodoDefinido;
	private JTextField tfNuevoRotulo, tfNuevoValor, tfRotuloDeNodoDefinido, tfValorDeNodoDefinido;
	private JButton btnIngresarValores;
	private boolean seCreoArbol;
	private boolean seCreoRaiz;
	
	public InterfazGrafica() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(10,10, 854, 480);
		setResizable(false);
		setTitle("Operador de arbol general");
		getContentPane().setLayout(null);
		
		this.seCreoArbol = false;
		this.seCreoRaiz = false;
		this.programa = new BackgroundPrograma();
//		this.armarContenedores();
		this.armarComboBox();
		this.armarPanelIngresarValores();
	}
	
	private void armarPanelIngresarValores() {
		pnOperacion = new JPanel();
		pnOperacion.setLayout(null);
		pnOperacion.setBounds( 8, 40,252,304);
		pnOperacion.setBorder(BorderFactory.createTitledBorder("Ingresa valores"));
		getContentPane().add(pnOperacion);
		
		this.armarPanelNuevoNodo();
		this.armarPanelNodoDefinido();
		
		btnIngresarValores = new JButton("Crear �rbol");
		btnIngresarValores.setBounds( 10, 260, 232, 24);
		btnIngresarValores.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnIngresarValores.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!seCreoArbol) {
					//Se crea el arbol y cambia a true al booleano
					seCreoArbol = programa.crearArbol();
					
					//Lanzo una ventana emergente de exito
					crearVentanaEmergenteExitosa("�Se creo el �rbol de forma exitosa!");
					//Establezco editables los textField
					tfNuevoRotulo.setEditable(true);
					tfNuevoValor.setEditable(true);
					//Altero el texto del boton
					btnIngresarValores.setText("Ingresar valores");
					//Establezco a la segunda opcion del combo box para crear raiz
					cbAction.setSelectedIndex(1);
					
				} else {
					//Obtener el indice de la opcion seleccionada en el combobox
					int intComboBox = cbAction.getSelectedIndex();
					//Si no se creo la raiz y el combobox no esta en la opcion 2 "Crear nodo"
					if(!seCreoRaiz && intComboBox == 1) {
						//Obtener los strings de los JTextField
						String sRotuloIngresado = tfNuevoRotulo.getText();
						String sValorIngresado = tfNuevoValor.getText();
						int intValorIngresado = Integer.parseInt(sValorIngresado);
						
						try {
							seCreoRaiz = programa.crearRaiz(sRotuloIngresado, intValorIngresado);
						} catch (InvalidOperationException e1) {
							crearVentanaEmergenteFallida("Hubo un error al crear la raiz");
						}
						
						if(seCreoRaiz) {
							//Lanzo mensaje de exito
							crearVentanaEmergenteExitosa("<html>�Se creo el nodo raiz del arbol de forma exitosa!"+
									"<p>* Rotulo de la raiz: "+sRotuloIngresado+"<p>* Valor de la raiz: "+intValorIngresado+"</html>");
							
							//Limpio los JTextField
							limpiarInputs();
							
							//Establezco editables a los textField de nodo ingresado
							tfRotuloDeNodoDefinido.setEditable(true);
							tfValorDeNodoDefinido.setEditable(true);
							
							//Establezco un nuevo titulo para el borde del pane del nuevo nodo
							pnNuevoNodo.setBorder(BorderFactory.createTitledBorder("Crear nuevo nodo"));
							
						}
					} else {
						boolean seCompleto = false;
						
						String sRotuloIngresado = tfNuevoRotulo.getText();
						String sValorIngresado = tfNuevoValor.getText();
						int intValorIngresado = Integer.parseInt(sValorIngresado);
						
						String sRotuloDeNodoDefinido = tfRotuloDeNodoDefinido.getText();
						String sValorDeNodoDefinido = tfValorDeNodoDefinido.getText();
						int intValorDeNodoDefinido = Integer.parseInt(sValorDeNodoDefinido);
						
						if( intComboBox == 1 ) {
							try {
								seCompleto = programa.agregarNodo(sRotuloIngresado, intValorIngresado, sRotuloDeNodoDefinido, intValorDeNodoDefinido);
							} catch (InvalidPositionException | GInvalidOperationException e1) {
								crearVentanaEmergenteFallida(e1.getMessage());
							} 
							if(seCompleto) {
								crearVentanaEmergenteExitosa("<html>"
										+ "Se a�adio un nuevo hijo al nodo ( "+sRotuloDeNodoDefinido+", "+intValorDeNodoDefinido+")"
												+ "<p>* Rotulo de la raiz: "+sRotuloIngresado+"<p>* Valor de la raiz: "+intValorIngresado+"</html>");
							}
						}
						
						limpiarInputs();
					}
					
					
					
					
				}
				
			}
		});
		
		pnOperacion.add(btnIngresarValores);
	}
	
	private void armarPanelNuevoNodo() {
		pnNuevoNodo = new JPanel();
		pnNuevoNodo.setLayout(null);
		pnNuevoNodo.setBounds( 8, 16, 236, 120);
		pnNuevoNodo.setBorder(BorderFactory.createTitledBorder("Crear nodo raiz"));
		pnOperacion.add(pnNuevoNodo);
		
		//Creo el label de ingresar rotulo del nodo
		lbNuevoRotulo = new JLabel("Rotulo del nodo");
		lbNuevoRotulo.setBounds( 8, 20 , 120, 20);
		lbNuevoRotulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el rotulo del nodo
		tfNuevoRotulo = new JTextField();
		tfNuevoRotulo.setBounds( 8, 40, 220, 20);
		tfNuevoRotulo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfNuevoRotulo.setEditable(false);
		//Creo el label de ingresar valor del nodo
		lbNuevoValor = new JLabel("Valor del nodo");
		lbNuevoValor.setBounds( 8, 68, 120, 20);
		lbNuevoValor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el valor del nodo
		tfNuevoValor= new JTextField();
		tfNuevoValor.setBounds( 8, 88, 220, 20);
		tfNuevoValor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfNuevoValor.setEditable(false);
		
		pnNuevoNodo.add(lbNuevoRotulo);
		pnNuevoNodo.add(tfNuevoRotulo);
		pnNuevoNodo.add(lbNuevoValor);
		pnNuevoNodo.add(tfNuevoValor);
	}
	
	private void armarPanelNodoDefinido() {
		pnNodoDefinido = new JPanel();
		pnNodoDefinido.setLayout(null);
		pnNodoDefinido.setBounds( 8, 136, 236, 120);
		pnNodoDefinido.setBorder(BorderFactory.createTitledBorder("Nodo creado"));
		pnOperacion.add(pnNodoDefinido);
		
		//Creo el label de ingresar rotulo del nodo
		lbRotuloDeNodoDefinido = new JLabel("Rotulo del nodo");
		lbRotuloDeNodoDefinido.setBounds( 8, 20 , 120, 20);
		lbRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el rotulo del nodo
		tfRotuloDeNodoDefinido = new JTextField();
		tfRotuloDeNodoDefinido.setBounds( 8, 40, 220, 20);
		tfRotuloDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfRotuloDeNodoDefinido.setEditable(false);
		//Creo el label de ingresar valor del nodo
		lbValorDeNodoDefinido = new JLabel("Valor del nodo");
		lbValorDeNodoDefinido.setBounds( 8, 68, 120, 20);
		lbValorDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 16));
		//TextField para ingresar el valor del nodo
		tfValorDeNodoDefinido = new JTextField();
		tfValorDeNodoDefinido.setBounds( 8, 88, 220, 20);
		tfValorDeNodoDefinido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tfValorDeNodoDefinido.setEditable(false);
		
		pnNodoDefinido.add(lbRotuloDeNodoDefinido);
		pnNodoDefinido.add(tfRotuloDeNodoDefinido);
		pnNodoDefinido.add(lbValorDeNodoDefinido);
		pnNodoDefinido.add(tfValorDeNodoDefinido);
	}
	
	private void armarComboBox() {
		cbAction = new JComboBox<String>();
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cbAction.setBounds( 8, 8, 250, 24);
		cbAction.setBackground(Color.white);
		
		cbAction.addItem("1- Crear �rbol");
		cbAction.addItem("2- Agregar nodo");
		cbAction.addItem("3- Eliminar nodo");
		cbAction.addItem("4- Obtener grados");
		cbAction.addItem("5- Obtener grado del �rbol");
		cbAction.addItem("6- Obtener camino");
		cbAction.addItem("7- Mostrar recorrido pre-orden");
		cbAction.addItem("8- Mostrar recorrido por niveles");
		cbAction.addItem("9- Mostrar recorrido post-orden");
		cbAction.addItem("10- Eliminar nodos grado k");
		cbAction.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		cbAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Numero de la opcion coincide con el indice de la combo box
				 * 0- Crear �rbol
				 * 1- Agregar nodo
				 * 2- Eliminar nodo
				 * 3- Obtener grados
				 * 4- Obtener grado del �rbol
				 * 5- Obtener camino
				 * 6- Mostrar recorrido pre-orden
				 * 7- Mostrar recorrido por niveles
				 * 8- Mostrar recorrido post-orden
				 * 9- Eliminar nodos grado k
				 */
				//Obtengo el indice de la opcion selecciona de la combo box
				int indexComboBox = cbAction.getSelectedIndex();
				if(!seCreoArbol && indexComboBox != 0) {
					crearVentanaEmergenteFallida("Aun no se creo el �rbol");
					//Establezco que debe elegir la primera opcion del combobox
					cbAction.setSelectedIndex(0);
				} 
			
				if(seCreoArbol) {
					
					if( !seCreoRaiz && indexComboBox != 1) {
						crearVentanaEmergenteFallida("Aun no se creo la raiz del �rbol");
						//Establezco que debe elegir la segunda opcion del combobox
						cbAction.setSelectedIndex(1);
					} else if(indexComboBox == 0) {
						crearVentanaEmergenteFallida("Ya se creo el �rbol");
						cbAction.setSelectedIndex(1);
					} else if(seCreoRaiz && indexComboBox == 1) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(true);
						tfNuevoValor.setEditable(true);
						tfRotuloDeNodoDefinido.setEditable(true);
						tfValorDeNodoDefinido.setEditable(true);
					} else if(indexComboBox == 2) {
						limpiarInputs();
						tfNuevoRotulo.setEditable(false);
						tfNuevoValor.setEditable(false);
						tfRotuloDeNodoDefinido.setEditable(true);
						tfValorDeNodoDefinido.setEditable(true);
					}
				}
				
				
			}
		});
		
		getContentPane().add(cbAction);
	}
	
	private void crearVentanaEmergenteExitosa(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion exitosa", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void crearVentanaEmergenteFallida(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Operacion fallida", JOptionPane.ERROR_MESSAGE);
	}
	
	private void limpiarInputs() {
		tfNuevoRotulo.setText(null);
		tfNuevoValor.setText(null);
		tfRotuloDeNodoDefinido.setText(null);
		tfValorDeNodoDefinido.setText(null);
	}
	
	public static void main (String [] args) {
		InterfazGrafica interfazGrafica = new InterfazGrafica();
		interfazGrafica.setVisible(true);
	}
}
