package controller;

import dao.*;
import entity.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private ComboBox<String> tableSelector;
    @FXML private GridPane queryPane;
    @FXML private TableView<Map<String, Object>> dataTable;
    @FXML private Label statusLabel;

    // 存储当前查询条件的输入框
    private Map<String, TextField> queryFields = new HashMap<>();

    // DAO实例
    private StudentDAO studentDAO = new StudentDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private DormDAO dormDAO = new DormDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private GradeDAO gradeDAO = new GradeDAO();

    // 当前选择的表名
    private String currentTable;

    // 表格列定义信息
    private Map<String, List<TableColumnInfo>> tableColumnsInfo = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化表格定义信息
        initializeTableDefinitions();

        // 初始化表选择下拉框
        ObservableList<String> tables = FXCollections.observableArrayList(
                "student", "department", "dorm", "course", "grade");
        tableSelector.setItems(tables);
    }

    private void initializeTableDefinitions() {
        // 定义学生表字段信息
        List<TableColumnInfo> studentColumns = Arrays.asList(
                new TableColumnInfo("sno", "学号", "String", true),
                new TableColumnInfo("sname", "姓名", "String", false),
                new TableColumnInfo("sex", "性别", "String", false),
                new TableColumnInfo("sage", "年龄", "Integer", false),
                new TableColumnInfo("dno", "院系编号", "String", false),
                new TableColumnInfo("dormno", "宿舍编号", "String", false)
        );
        tableColumnsInfo.put("student", studentColumns);

        // 定义院系表字段信息
        List<TableColumnInfo> departmentColumns = Arrays.asList(
                new TableColumnInfo("dno", "院系编号", "String", true),
                new TableColumnInfo("dname", "院系名称", "String", false),
                new TableColumnInfo("head", "负责人", "String", false)
        );
        tableColumnsInfo.put("department", departmentColumns);

        // 定义宿舍表字段信息
        List<TableColumnInfo> dormColumns = Arrays.asList(
                new TableColumnInfo("dormno", "宿舍编号", "String", true),
                new TableColumnInfo("tele", "电话", "String", false)
        );
        tableColumnsInfo.put("dorm", dormColumns);

        // 定义课程表字段信息
        List<TableColumnInfo> courseColumns = Arrays.asList(
                new TableColumnInfo("cno", "课程编号", "String", true),
                new TableColumnInfo("cname", "课程名称", "String", false),
                new TableColumnInfo("cpno", "先修课程号", "String", false),
                new TableColumnInfo("credit", "学分", "Integer", false),
                new TableColumnInfo("teacher", "教师", "String", false)
        );
        tableColumnsInfo.put("course", courseColumns);

        // 定义成绩表字段信息
        List<TableColumnInfo> gradeColumns = Arrays.asList(
                new TableColumnInfo("sno", "学号", "String", true),
                new TableColumnInfo("cno", "课程编号", "String", true),
                new TableColumnInfo("score", "成绩", "Integer", false)
        );
        tableColumnsInfo.put("grade", gradeColumns);
    }

    @FXML
    private void handleTableSelection() {
        String selectedTable = tableSelector.getValue();
        if (selectedTable != null) {
            currentTable = selectedTable;
            setupQueryFields(selectedTable);
            setupTableColumns(selectedTable);
            refreshTable();
        }
    }

    private void setupQueryFields(String tableName) {
        // 清空现有查询条件
        queryPane.getChildren().clear();
        queryFields.clear();

        List<TableColumnInfo> columns = tableColumnsInfo.get(tableName);
        if (columns == null) return;

        int row = 0;
        int col = 0;

        for (TableColumnInfo column : columns) {
            // 添加标签
            Label label = new Label(column.getDisplayName() + ":");
            queryPane.add(label, col, row);

            // 添加输入框
            TextField field = new TextField();
            field.setPromptText("任意值");
            queryPane.add(field, col + 1, row);

            // 存储输入框引用
            queryFields.put(column.getColumnName(), field);

            // 更新下一个字段的位置
            col += 2;
            if (col >= 6) { // 每行3个字段
                col = 0;
                row++;
            }
        }
    }

    private void setupTableColumns(String tableName) {
        dataTable.getColumns().clear();
        dataTable.getItems().clear();

        List<TableColumnInfo> columns = tableColumnsInfo.get(tableName);
        if (columns == null) return;

        for (TableColumnInfo columnInfo : columns) {
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnInfo.getDisplayName());
            column.setCellValueFactory(data -> {
                Object value = data.getValue().get(columnInfo.getColumnName());
                return new SimpleObjectProperty<>(value);
            });
            dataTable.getColumns().add(column);
        }
    }

    @FXML
    private void handleQuery() {
        try {
            refreshTable();
            statusLabel.setText("查询成功");
        } catch (Exception e) {
            statusLabel.setText("查询失败: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "查询错误", null, "查询数据时发生错误: " + e.getMessage());
        }
    }

    private void refreshTable() {
        if (currentTable == null) return;

        try {
            List<?> dataList = null;

            // 获取数据
            switch (currentTable) {
                case "student":
                    dataList = studentDAO.getAll();
                    break;
                case "department":
                    dataList = departmentDAO.getAll();
                    break;
                case "dorm":
                    dataList = dormDAO.getAll();
                    break;
                case "course":
                    dataList = courseDAO.getAll();
                    break;
                case "grade":
                    dataList = gradeDAO.getAll();
                    break;
            }

            // 过滤数据
            List<Map<String, Object>> filteredData = new ArrayList<>();
            if (dataList != null) {
                for (Object obj : dataList) {
                    Map<String, Object> row = new HashMap<>();

                    // 根据表类型获取数据
                    if (currentTable.equals("student")) {
                        Student student = (Student) obj;
                        row.put("sno", student.getSno());
                        row.put("sname", student.getSname());
                        row.put("sex", student.getSex());
                        row.put("sage", student.getSage());
                        row.put("dno", student.getDno());
                        row.put("dormno", student.getDormno());
                    } else if (currentTable.equals("department")) {
                        Department dept = (Department) obj;
                        row.put("dno", dept.getDno());
                        row.put("dname", dept.getDname());
                        row.put("head", dept.getHead());
                    } else if (currentTable.equals("dorm")) {
                        Dorm dorm = (Dorm) obj;
                        row.put("dormno", dorm.getDormno());
                        row.put("tele", dorm.getTele());
                    } else if (currentTable.equals("course")) {
                        Course course = (Course) obj;
                        row.put("cno", course.getCno());
                        row.put("cname", course.getCname());
                        row.put("cpno", course.getCpno());
                        row.put("credit", course.getCredit());
                        row.put("teacher", course.getTeacher());
                    } else if (currentTable.equals("grade")) {
                        Grade grade = (Grade) obj;
                        row.put("sno", grade.getSno());
                        row.put("cno", grade.getCno());
                        row.put("score", grade.getScore());
                    }

                    // 应用查询过滤条件
                    boolean shouldInclude = true;
                    for (Map.Entry<String, TextField> entry : queryFields.entrySet()) {
                        String fieldName = entry.getKey();
                        String queryValue = entry.getValue().getText().trim();

                        if (!queryValue.isEmpty()) {
                            Object fieldValue = row.get(fieldName);
                            if (fieldValue == null) {
                                shouldInclude = false;
                                break;
                            }

                            String fieldValueStr = fieldValue.toString();
                            // 使用包含匹配而不是精确匹配
                            if (!fieldValueStr.toLowerCase().contains(queryValue.toLowerCase())) {
                                shouldInclude = false;
                                break;
                            }
                        }
                    }

                    if (shouldInclude) {
                        filteredData.add(row);
                    }
                }
            }

            dataTable.setItems(FXCollections.observableArrayList(filteredData));

        } catch (Exception e) {
            statusLabel.setText("加载数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddRecord() {
        if (currentTable == null) {
            showAlert(Alert.AlertType.WARNING, "提示", null, "请先选择一个表");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/EditDialog.fxml"));
            Parent root = loader.load();

            EditDialogController controller = loader.getController();
            controller.setTable(currentTable, tableColumnsInfo.get(currentTable), null);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("添加记录");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(root));

            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            dialogStage.showAndWait();

        } catch (Exception e) {
            statusLabel.setText("打开添加对话框失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateRecord() {
        if (currentTable == null) {
            showAlert(Alert.AlertType.WARNING, "提示", null, "请先选择一个表");
            return;
        }

        Map<String, Object> selectedData = dataTable.getSelectionModel().getSelectedItem();
        if (selectedData == null) {
            showAlert(Alert.AlertType.WARNING, "提示", null, "请先选择一条记录");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/EditDialog.fxml"));
            Parent root = loader.load();

            EditDialogController controller = loader.getController();
            controller.setTable(currentTable, tableColumnsInfo.get(currentTable), selectedData);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("更新记录");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(root));

            controller.setDialogStage(dialogStage);
            controller.setMainController(this);

            dialogStage.showAndWait();

        } catch (Exception e) {
            statusLabel.setText("打开更新对话框失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteRecord() {
        if (currentTable == null) {
            showAlert(Alert.AlertType.WARNING, "提示", null, "请先选择一个表");
            return;
        }

        Map<String, Object> selectedData = dataTable.getSelectionModel().getSelectedItem();
        if (selectedData == null) {
            showAlert(Alert.AlertType.WARNING, "提示", null, "请先选择一条记录");
            return;
        }

        // 确认删除
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setHeaderText("确定要删除选中的记录吗？");
        alert.setContentText("此操作无法撤销！");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = false;

                switch (currentTable) {
                    case "student":
                        success = studentDAO.delete(selectedData.get("sno"));
                        break;
                    case "department":
                        success = departmentDAO.delete(selectedData.get("dno"));
                        break;
                    case "dorm":
                        success = dormDAO.delete(selectedData.get("dormno"));
                        break;
                    case "course":
                        success = courseDAO.delete(selectedData.get("cno"));
                        break;
                    case "grade":
                        success = gradeDAO.delete(selectedData.get("sno"), selectedData.get("cno"));
                        break;
                }

                if (success) {
                    statusLabel.setText("删除记录成功");
                    refreshTable();
                } else {
                    statusLabel.setText("删除记录失败");
                }
            } catch (Exception e) {
                statusLabel.setText("删除失败: " + e.getMessage());
                showAlert(Alert.AlertType.ERROR, "删除错误", null, "删除记录时发生错误: " + e.getMessage());
            }
        }
    }

    public void refreshData() {
        refreshTable();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // 表格列信息类
    public static class TableColumnInfo {
        private String columnName;
        private String displayName;
        private String dataType;
        private boolean isPrimaryKey;

        public TableColumnInfo(String columnName, String displayName, String dataType, boolean isPrimaryKey) {
            this.columnName = columnName;
            this.displayName = displayName;
            this.dataType = dataType;
            this.isPrimaryKey = isPrimaryKey;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDataType() {
            return dataType;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }
    }
}