package controller;

import dao.*;
import entity.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDialogController {

    @FXML private Label titleLabel;
    @FXML private GridPane fieldPane;

    private Stage dialogStage;
    private MainController mainController;
    private String currentTable;
    private Map<String, Object> currentData;
    private List<MainController.TableColumnInfo> columnInfoList;
    private Map<String, Control> fieldControls = new HashMap<>();

    // DAO实例
    private StudentDAO studentDAO = new StudentDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private DormDAO dormDAO = new DormDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private GradeDAO gradeDAO = new GradeDAO();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setTable(String tableName, List<MainController.TableColumnInfo> columnInfoList, Map<String, Object> data) {
        this.currentTable = tableName;
        this.columnInfoList = columnInfoList;
        this.currentData = data;

        // 设置标题
        if (data == null) {
            titleLabel.setText("添加新" + getTableDisplayName(tableName) + "记录");
        } else {
            titleLabel.setText("更新" + getTableDisplayName(tableName) + "记录");
        }

        // 创建表单字段
        setupFields();
    }

    private void setupFields() {
        fieldPane.getChildren().clear();
        fieldControls.clear();

        int row = 0;
        for (MainController.TableColumnInfo columnInfo : columnInfoList) {
            Label label = new Label(columnInfo.getDisplayName() + ":");
            fieldPane.add(label, 0, row);

            Control field;

            // 根据数据类型创建不同的控件
            if (columnInfo.getDataType().equals("Integer")) {
                TextField textField = new TextField();
                textField.setPromptText("请输入" + columnInfo.getDisplayName());

                // 限制只能输入数字
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        textField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                field = textField;
            } else {
                TextField textField = new TextField();
                textField.setPromptText("请输入" + columnInfo.getDisplayName());
                field = textField;
            }

            // 如果是更新操作，填充现有数据
            if (currentData != null) {
                if (field instanceof TextField) {
                    Object value = currentData.get(columnInfo.getColumnName());
                    ((TextField) field).setText(value != null ? value.toString() : "");
                }

                // 主键字段在更新时不可编辑
                if (columnInfo.isPrimaryKey()) {
                    field.setDisable(true);
                }
            }

            fieldPane.add(field, 1, row);
            fieldControls.put(columnInfo.getColumnName(), field);

            row++;
        }
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            try {
                boolean success = false;

                switch (currentTable) {
                    case "student":
                        success = saveStudent();
                        break;
                    case "department":
                        success = saveDepartment();
                        break;
                    case "dorm":
                        success = saveDorm();
                        break;
                    case "course":
                        success = saveCourse();
                        break;
                    case "grade":
                        success = saveGrade();
                        break;
                }

                if (success) {
                    mainController.refreshData();
                    dialogStage.close();
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "保存错误", null, "保存记录时发生错误: " + e.getMessage());
            }
        }
    }

    private boolean saveStudent() throws Exception {
        Student student = new Student();
        student.setSno(getFieldText("sno"));
        student.setSname(getFieldText("sname"));
        student.setSex(getFieldText("sex"));
        student.setSage(Integer.parseInt(getFieldText("sage")));
        student.setDno(getFieldText("dno"));
        student.setDormno(getFieldText("dormno"));

        if (currentData == null) {
            return studentDAO.add(student);
        } else {
            return studentDAO.update(student);
        }
    }

    private boolean saveDepartment() throws Exception {
        Department dept = new Department();
        dept.setDno(getFieldText("dno"));
        dept.setDname(getFieldText("dname"));
        dept.setHead(getFieldText("head"));

        if (currentData == null) {
            return departmentDAO.add(dept);
        } else {
            return departmentDAO.update(dept);
        }
    }

    private boolean saveDorm() throws Exception {
        Dorm dorm = new Dorm();
        dorm.setDormno(getFieldText("dormno"));
        dorm.setTele(getFieldText("tele"));

        if (currentData == null) {
            return dormDAO.add(dorm);
        } else {
            return dormDAO.update(dorm);
        }
    }

    private boolean saveCourse() throws Exception {
        Course course = new Course();
        course.setCno(getFieldText("cno"));
        course.setCname(getFieldText("cname"));

        String cpno = getFieldText("cpno");
        course.setCpno(cpno.isEmpty() ? null : cpno);

        course.setCredit(Integer.parseInt(getFieldText("credit")));
        course.setTeacher(getFieldText("teacher"));

        if (currentData == null) {
            return courseDAO.add(course);
        } else {
            return courseDAO.update(course);
        }
    }

    private boolean saveGrade() throws Exception {
        Grade grade = new Grade();
        grade.setSno(getFieldText("sno"));
        grade.setCno(getFieldText("cno"));
        grade.setScore(Integer.parseInt(getFieldText("score")));

        if (currentData == null) {
            return gradeDAO.add(grade);
        } else {
            return gradeDAO.update(grade);
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();

        for (MainController.TableColumnInfo columnInfo : columnInfoList) {
            String fieldName = columnInfo.getColumnName();
            Control control = fieldControls.get(fieldName);

            if (control instanceof TextField) {
                TextField textField = (TextField) control;
                String value = textField.getText().trim();

                // 主键和非空字段必须有值
                if (columnInfo.isPrimaryKey() && value.isEmpty()) {
                    errorMessage.append(columnInfo.getDisplayName()).append("不能为空\n");
                }

                // 数字类型字段必须是有效数字
                if (columnInfo.getDataType().equals("Integer") && !value.isEmpty()) {
                    try {
                        Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        errorMessage.append(columnInfo.getDisplayName()).append("必须是有效数字\n");
                    }
                }
            }
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "输入错误", "请修正以下问题:", errorMessage.toString());
            return false;
        }

        return true;
    }

    private String getFieldText(String fieldName) {
        Control control = fieldControls.get(fieldName);
        if (control instanceof TextField) {
            return ((TextField) control).getText().trim();
        }
        return "";
    }

    private String getTableDisplayName(String tableName) {
        switch (tableName) {
            case "student": return "学生";
            case "department": return "院系";
            case "dorm": return "宿舍";
            case "course": return "课程";
            case "grade": return "成绩";
            default: return tableName;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}