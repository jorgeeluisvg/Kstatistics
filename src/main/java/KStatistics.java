import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/*
 * @Author: Jorge L Villanueva
 *
 */

public class KStatistics  extends JFrame {
ArrayList<Double> data1 = new ArrayList<>();
ArrayList<Double> data2 = new ArrayList<>();
DefaultCategoryDataset datos = new DefaultCategoryDataset();
int s2021;
int s2022;
int bandera = 0;

   public KStatistics(){
       /* ALTA */
       super("KOKY STATISTICS PROGRAM");
       setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       setSize(610, 750);
       setLayout(null);
       getContentPane().setBackground(Color.decode("#e2e2e2"));
       JLabel Jtitulo = new JLabel("KOKY STATISTICS PROGRAM");
       Jtitulo.setBounds(220,-80,200,200);

       JTextField diatxt = new JTextField("Dia");
       diatxt.setBounds(225,590,150,30);
       this.add(diatxt);

       JTextField mestxt = new JTextField("Mes");
       mestxt.setBounds(225,630,150,30);
       this.add(mestxt);

       JButton Bexcel = new JButton("Generar Excel");
       Bexcel.setBounds(50,510,150,60);
       Bexcel.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               JFileChooser fileChooser = new JFileChooser();
               fileChooser.showSaveDialog(null);
               File archivo = new File(fileChooser.getSelectedFile()+".csv");
               try{
                   BufferedWriter salida = new BufferedWriter(new FileWriter(archivo));
                   salida.write(String.valueOf("Ventas 2021"+"\n"));

                   for (int i = 0; i <data1.size() ; i++) {
                       salida.write(String.valueOf(data1.get(i)+"\n"));
                   }
                   salida.write("\n");
                   salida.write(String.valueOf("Ventas 2022"+"\n"));
                   for (int j = 0; j <data2.size() ; j++) {
                       salida.write(String.valueOf(data2.get(j)+"\n"));
                   }
                   salida.close();
               }catch (Exception i){

               }
           }
       });
       this.add(Bexcel);

       JButton Bvestaciones = new JButton("Ventas Por Estaciones");
       Bvestaciones.setBounds(225,510,150,60);
       Bvestaciones.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               //ventas por sector empresa
               Connection connection = null;
               try {
                   connection = DriverManager.getConnection("" +
                           "jdbc:mysql://localhost:3306/ventas","root","admin");
                   Statement statement = connection.createStatement();
                   ResultSet resultSet3 = statement.executeQuery("SELECT distinct vta_pc, sum(vta_total) TOTAL_$MXM from ventas WHERE vta_fecha between '2021-"+mestxt.getText()+"-"+diatxt.getText()+"00:00:00' and '2021-"+mestxt.getText()+"-"+diatxt.getText()+" 23:59:59' group by vta_pc;");
                   while(resultSet3.next()) {
                       System.out.println(resultSet3.getString("vta_pc")+" "+resultSet3.getString("TOTAL_$MXM") );
                       JFileChooser fileChooser = new JFileChooser();
                       fileChooser.showSaveDialog(null);
                       File archivo = new File(fileChooser.getSelectedFile()+".txt");
                       try{
                           BufferedWriter salida = new BufferedWriter(new FileWriter(archivo));
                           salida.write(resultSet3.getString("vta_pc")+" "+resultSet3.getString("TOTAL_$MXM")+"\n");
                           salida.close();
                       }catch (Exception i){

                       }
                   }
               } catch (SQLException ex) {

               }

           }
       });
       this.add(Bvestaciones);

       JButton Bgrafica = new JButton("Generar Grafica");
       Bgrafica.setBounds(400,510,150,60);
       Bgrafica.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if (bandera == 0) {
                   setSize(1290, 750);
                   bandera = 1;
               }
               else{
                   setSize(610, 750);
                   bandera = 0;
               }

           }
       });

       this.add(Bgrafica);
       this.add(Jtitulo);
       /* ALTA */

       try{
           /* TABLA */
           DefaultTableModel model = new DefaultTableModel();
           model.addColumn("Ventas 2021");
           JTable tabla = new JTable();
           tabla.setModel(model);
           JScrollPane scroll = new JScrollPane(tabla);
           scroll.setBounds(50,50,500,200);
           this.add(scroll);

           DefaultTableModel model2 = new DefaultTableModel();
           model2.addColumn("Ventas 2022");
           JTable tabla2 = new JTable();
           tabla2.setModel(model2);
           JScrollPane scroll2 = new JScrollPane(tabla2);
           scroll2.setBounds(50,280,500,200);
           this.add(scroll2);
           /* TABLA */

           /* CONEXION BD*/
           Connection connection = DriverManager.getConnection("" +
                   "jdbc:mysql://localhost:3306/ventas","root","admin");

           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("select  vta_total from ventas where vta_fecha between'2021-07-02 00:00:00' and '2021-07-02 23:59:59';");

           //ventas 2020
           while(resultSet.next()) {
                //Añadimos los valores a datos pare representarlos en la grafica
               data1.add(Double.valueOf(resultSet.getString("vta_total")));
               model.addRow(new Object[] {resultSet.getString("vta_total")});//añade valores a tabla
           }
           //ventas 2021
           ResultSet resultSet2 = statement.executeQuery("select  vta_total from ventas where vta_fecha between'2022-01-02 00:00:00' and '2022-07-02 23:59:59';");
           while(resultSet2.next()) {
               //Añadimos los valores a datos pare representarlos en la grafica
               data2.add(Double.valueOf(resultSet2.getString("vta_total")));
               model2.addRow(new Object[] {resultSet2.getString("vta_total")});//añade valores a tabla
           }



           /* CONEXION BD*/

           for (int i = 0; i < data1.size() ; i++) {
               s2021 += data1.get(i);
           }
           for (int j = 0; j < data2.size() ; j++) {
               s2022 += data2.get(j);
           }

           datos.setValue(s2021,"ventas","2021");
           datos.setValue(s2022,"ventas","2022");

           JFreeChart grafico = ChartFactory.createBarChart3D(
                   "Histograma ventas",
                   "Ventas 2021 - Ventas 2022",
                   "ventas",
                   datos,
                   PlotOrientation.VERTICAL,
                   true,
                   true,
                   false
           );

           ChartPanel panel = new ChartPanel(grafico);
           panel.setMouseWheelEnabled(true);
           panel.setBounds(600,50,600,500);
           this.add(panel);
           setVisible(true);
       } catch(Exception e){
           e.printStackTrace();
       }

   }

   public static void main(String[] args) {
   new KStatistics();
   }

}
