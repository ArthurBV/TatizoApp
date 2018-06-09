/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TatizoApp;

import control.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author vanis
 */
public class AdminTraining extends javax.swing.JFrame {

    private String ruteAction;
    private String ruteUser;
    private String ruteReport;
    private String rutePathTraining;
    private ArrayList<Action> cases;
    private ArrayList<Report> reports;
    private ArrayList<Parameter> parameters;
    private int caseSelected;
    private String roleSelected;
    private int aCase;
    private float bCase;
    private int minCase;
    private int maxCase;
    private DefaultTableModel modelAction;
    private DefaultTableModel modelReport;
    ActionManagement actionManage;
    ParameterManagement parametersManage;
    ReportManagement reportManage;
    UserManagement userManage;
    User user;

    /**
     * Creates new form AdminTraining
     */
    public AdminTraining(Login login, String userName) {
        this.setVisible(true);
        initComponents();
        this.jLabelUser.setText(userName);
        this.ruteUser = "./tatizoFiles/users.txt";
        this.userManage = new UserManagement(ruteUser);
        this.ruteAction = "./tatizoFiles/adminCases.txt";
        this.actionManage = new ActionManagement(ruteAction);
        this.ruteReport = "./tatizoReports/reportsUsers.txt";
        this.reportManage = new ReportManagement(ruteReport);
        modelAction = (DefaultTableModel) jTableAction.getModel();
        modelReport = (DefaultTableModel) jTableReport.getModel();
        this.caseSelected = 1;
        this.roleSelected = "Ayudador";
        this.aCase = 0;
        this.bCase = 0;
        this.minCase = 0;
        this.maxCase = 0;
        this.getAction();
        this.fillTableReport();
        this.listenTable();
    }

    private Action getAction() {
        ArrayList<Action> cases = new ArrayList();
        FileReader archive;
        BufferedReader br;
        String registry;
        Action stage = null;

        try {
            archive = new FileReader(ruteAction);
            br = new BufferedReader(archive);
            while ((registry = br.readLine()) != null) {
                String[] fields = registry.split("&&");
                if (caseSelected == Integer.parseInt(fields[0])) {
                    stage = new Action(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Float.parseFloat(fields[2]),
                            Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], fields[6]);
                    jTextAreaFormula.setText("Eoptimo=[" + Integer.parseInt(fields[1]) + "*Ea)/(" + Float.parseFloat(fields[2]) + "*Ec*t)]*100%, Si Eoptimo<0, Entonces Eoptimo = 0");
                    aCase = Integer.parseInt(fields[1]);
                    bCase = Float.parseFloat(fields[2]);
                    minCase = Integer.parseInt(fields[3]);
                    maxCase = Integer.parseInt(fields[3]);
                    rutePathTraining = fields[5];
                    ParameterManagement parametersManage = new ParameterManagement(rutePathTraining);
                    parameters = parametersManage.getAll(rutePathTraining);
                    this.fillTableAction();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ActionManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stage;
    }

    private void fillTableAction() {
        modelAction.setRowCount(0);
        int sizeArray = parameters.size();
        String registry;
        for (int i = 0; i < sizeArray; i++) {
            registry = parameters.get(i).toString();
            String[] fields = registry.split("&&");
            modelAction.addRow(fields);
        }
    }

    private void fillTableReport() {
        ReportManagement reportManage = new ReportManagement(ruteReport);
        reports = reportManage.getAll(ruteReport);
        modelReport.setRowCount(0);
        int sizeArray = reports.size();
        String registry;
        for (int i = 0; i < sizeArray; i++) {
            registry = reports.get(i).toString();
            String[] fields = registry.split("&&");
            modelReport.addRow(fields);
        }

    }

    private void listenTable() {
        jTableAction.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                jTextAction.setText(jTableAction.getValueAt(jTableAction.getSelectedRow(), 1).toString());
                //Eayudador
                if (jTableAction.getValueAt(jTableAction.getSelectedRow(), 3).toString().equals("sale")
                        || jTableAction.getValueAt(jTableAction.getSelectedRow(), 2).toString().equals("no aplica")
                        || jTableAction.getValueAt(jTableAction.getSelectedRow(), 2).toString().equals("	")) {
                    jTextAreaResult.setText("Eoptimo= 0");
                } else {
                    int eaParameter = Integer.parseInt(jTableAction.getValueAt(jTableAction.getSelectedRow(), 2).toString());
                    Float ecParameter = Float.parseFloat(jTableAction.getValueAt(jTableAction.getSelectedRow(), 3).toString());
                    int tParameter = Integer.parseInt(jTableAction.getValueAt(jTableAction.getSelectedRow(), 4).toString());
                    float result = (aCase * eaParameter) / (bCase * ecParameter * tParameter) * 100;
                    jTextAreaResult.setText("Eoptimo= " + result);
                }

            }
        });
    }

    private void createUser() {
        boolean valid = false;
        ArrayList<User> users = userManage.getAll();
        String nameUser = this.jTextFieldUser.getText();
        char pasword[] = this.jPasswordField.getPassword();
        String passworUser = new String(pasword);
        char paswordR[] = this.jRepeatPasswordField.getPassword();
        String passwordRUser = new String(paswordR);

        if (!nameUser.isEmpty()) {
            do {
                if (users.isEmpty()) {
                    valid = true;
                    break;
                }
                for (User employee : users) {
                    if (employee.getUserName().equals(nameUser)) {
                        JOptionPane.showMessageDialog(null, "El nombre de usuario ya esta en uso\n" + "Por favor vuelva a intentarlo");
                        valid = false;
                        break;
                    } else {
                        valid = true;
                    }
                }
                break;
            } while (!valid);

            if (valid) {
                if (passworUser.equals(passwordRUser)) {
                    user = new User(nameUser, passworUser, roleSelected, this.jTextFieldName.getText(), this.jTextFieldFamilyName.getText(), this.jTextFieldID.getText());
                    this.userManage.saveUser(user, ruteUser);
                    JOptionPane.showMessageDialog(null, "Guardado exitosamente");
                    jTextFieldUser.setText("");
                    jTextFieldName.setText("");
                    jTextFieldFamilyName.setText("");
                    jTextFieldID.setText("");
                    jPasswordField.setText("");
                    jRepeatPasswordField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no concuerdan\n" + "Por favor vuelva a intentarlo");
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelUser = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jComboBoxCase = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAction = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAction = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextAreaFormula = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaResult = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableReport = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButtonCreate = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldUser = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jLabel9 = new javax.swing.JLabel();
        jRepeatPasswordField = new javax.swing.JPasswordField();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldFamilyName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldID = new javax.swing.JTextField();
        jComboBoxRole = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel1.setBackground(new java.awt.Color(165, 216, 103));
        jPanel1.setForeground(new java.awt.Color(0, 134, 67));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 600));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 600));

        jLabelUser.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabelUser.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 48)); // NOI18N
        jLabel2.setText("Tatizo");

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jComboBoxCase.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jComboBoxCase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Escenario 1", "Escenario 2", "Escenario 3", "Escenario 4", "Escenario 5" }));
        jComboBoxCase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCaseActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel4.setText("Escoja un escenario");

        jTableAction.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTableAction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Acción", "Tiempo(min)", "Ea", "Ec", "t"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAction.setGridColor(new java.awt.Color(165, 216, 103));
        jTableAction.setSelectionBackground(new java.awt.Color(0, 134, 67));
        jScrollPane1.setViewportView(jTableAction);
        if (jTableAction.getColumnModel().getColumnCount() > 0) {
            jTableAction.getColumnModel().getColumn(0).setMinWidth(45);
            jTableAction.getColumnModel().getColumn(0).setPreferredWidth(45);
            jTableAction.getColumnModel().getColumn(0).setMaxWidth(45);
            jTableAction.getColumnModel().getColumn(1).setMinWidth(475);
            jTableAction.getColumnModel().getColumn(1).setPreferredWidth(475);
            jTableAction.getColumnModel().getColumn(1).setMaxWidth(475);
            jTableAction.getColumnModel().getColumn(2).setMinWidth(80);
            jTableAction.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTableAction.getColumnModel().getColumn(2).setMaxWidth(80);
            jTableAction.getColumnModel().getColumn(3).setMinWidth(50);
            jTableAction.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTableAction.getColumnModel().getColumn(3).setMaxWidth(50);
            jTableAction.getColumnModel().getColumn(4).setMinWidth(50);
            jTableAction.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTableAction.getColumnModel().getColumn(4).setMaxWidth(50);
            jTableAction.getColumnModel().getColumn(5).setMinWidth(50);
            jTableAction.getColumnModel().getColumn(5).setPreferredWidth(50);
            jTableAction.getColumnModel().getColumn(5).setMaxWidth(50);
        }

        jTextAction.setEditable(false);
        jTextAction.setColumns(20);
        jTextAction.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextAction.setLineWrap(true);
        jTextAction.setRows(5);
        jScrollPane2.setViewportView(jTextAction);

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("Acción");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Formula");

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel7.setText("Resultado");

        jTextAreaFormula.setEditable(false);
        jTextAreaFormula.setColumns(20);
        jTextAreaFormula.setLineWrap(true);
        jTextAreaFormula.setRows(5);
        jScrollPane4.setViewportView(jTextAreaFormula);

        jTextAreaResult.setEditable(false);
        jTextAreaResult.setColumns(20);
        jTextAreaResult.setLineWrap(true);
        jTextAreaResult.setRows(5);
        jScrollPane3.setViewportView(jTextAreaResult);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxCase, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Parametrización", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 764, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ruta Crítica", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jTableReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Nombre ayudador", "Cliente", "Tiempo del sorpote", "% Esfuerzo"
            }
        ));
        jScrollPane5.setViewportView(jTableReport);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Reporte", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jButtonCreate.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButtonCreate.setText("Crear");
        jButtonCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton1.setText("Borrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel8.setText("Usuario nuevo");

        jTextFieldUser.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel1.setText("Asignar contraseña");

        jPasswordField.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel9.setText("Repita contraseña");

        jRepeatPasswordField.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRepeatPasswordField)
                    .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUser, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel8))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRepeatPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel11.setText("Nombres");

        jTextFieldName.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel12.setText("Apellidos");

        jTextFieldFamilyName.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel13.setText("ID (registrado en el sistema)");

        jTextFieldID.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(jLabel11))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jLabel12)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(68, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldFamilyName, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(90, 90, 90))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldFamilyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jComboBoxRole.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jComboBoxRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ayudador", "Administrador" }));
        jComboBoxRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRoleActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel10.setText("Rol del usuario");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCreate)
                    .addComponent(jComboBoxRole, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(206, 206, 206))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jComboBoxRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCreate)
                    .addComponent(jButton1))
                .addGap(80, 80, 80))
        );

        jTabbedPane1.addTab("Crear Usuarios", jPanel5);

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Beta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(315, 315, 315))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelUser)
                                .addGap(35, 35, 35))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(21, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelUser))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxCaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCaseActionPerformed
        caseSelected = jComboBoxCase.getSelectedIndex() + 1;
        this.getAction();
    }//GEN-LAST:event_jComboBoxCaseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextFieldUser.setText("");
        jTextFieldName.setText("");
        jTextFieldFamilyName.setText("");
        jTextFieldID.setText("");
        jPasswordField.setText("");
        jRepeatPasswordField.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateActionPerformed
        this.createUser();
    }//GEN-LAST:event_jButtonCreateActionPerformed

    private void jComboBoxRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRoleActionPerformed
        if (jComboBoxRole.getSelectedIndex() == 0) {
            roleSelected = "Ayudador";
        } else {
            roleSelected = "Administrador";
        }
    }//GEN-LAST:event_jComboBoxRoleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonCreate;
    private javax.swing.JComboBox<String> jComboBoxCase;
    private javax.swing.JComboBox<String> jComboBoxRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JPasswordField jRepeatPasswordField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAction;
    private javax.swing.JTable jTableReport;
    private javax.swing.JTextArea jTextAction;
    private javax.swing.JTextArea jTextAreaFormula;
    private javax.swing.JTextArea jTextAreaResult;
    private javax.swing.JTextField jTextFieldFamilyName;
    private javax.swing.JTextField jTextFieldID;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldUser;
    // End of variables declaration//GEN-END:variables
}
