package Console;

import dao.*;
import entity.*;

import java.util.List;
import java.util.Scanner;

public class Console {
    private Scanner scanner = new Scanner(System.in);
    
    // 直接使用DAO实现类
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private DormDAO dormDAO = new DormDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private GradeDAO gradeDAO = new GradeDAO();
    
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            showMainMenu();
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    manageDepartments();
                    break;
                case 2:
                    manageDorms();
                    break;
                case 3:
                    manageCourses();
                    break;
                case 4:
                    manageStudents();
                    break;
                case 5:
                    manageGrades();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
        
        System.out.println("程序已退出。");
    }
    
    private void showMainMenu() {
        System.out.println("\n===== 学生信息管理系统 =====");
        System.out.println("1. 院系管理");
        System.out.println("2. 宿舍管理");
        System.out.println("3. 课程管理");
        System.out.println("4. 学生管理");
        System.out.println("5. 成绩管理");
        System.out.println("0. 退出系统");
    }
    
    // 院系管理
    private void manageDepartments() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n----- 院系管理 -----");
            System.out.println("1. 查看所有院系");
            System.out.println("2. 添加院系");
            System.out.println("3. 更新院系");
            System.out.println("4. 删除院系");
            System.out.println("5. 查询院系");
            System.out.println("0. 返回主菜单");
            
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    listAllDepartments();
                    break;
                case 2:
                    addDepartment();
                    break;
                case 3:
                    updateDepartment();
                    break;
                case 4:
                    deleteDepartment();
                    break;
                case 5:
                    queryDepartment();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
    }
    
    private void listAllDepartments() {
        try {
            List<Department> deptList = departmentDAO.getAll();
            if (deptList != null && !deptList.isEmpty()) {
                System.out.println("\n院系列表：");
                for (Department dept : deptList) {
                    System.out.println(dept);
                }
            } else {
                System.out.println("暂无院系信息。");
            }
        } catch (Exception e) {
            System.out.println("查询所有院系失败：" + e.getMessage());
        }
    }
    
    private void addDepartment() {
        System.out.println("\n添加新院系：");
        String dno = getStringInput("请输入院系编号（4位字符）：");
        String dname = getStringInput("请输入院系名称：");
        String head = getStringInput("请输入院系负责人：");
        
        Department dept = new Department(dno, dname, head);
        try {
            if (departmentDAO.add(dept)) {
                System.out.println("院系添加成功！");
            } else {
                System.out.println("院系添加失败！");
            }
        } catch (Exception e) {
            System.out.println("添加院系失败：" + e.getMessage());
        }
    }
    
    private void updateDepartment() {
        System.out.println("\n更新院系信息：");
        String dno = getStringInput("请输入要更新的院系编号：");
        
        try {
            Department dept = departmentDAO.getById(dno);
            
            if (dept == null) {
                System.out.println("未找到该院系！");
                return;
            }
            
            System.out.println("当前院系信息：" + dept);
            String dname = getStringInput("请输入新院系名称（直接回车保持不变）：");
            String head = getStringInput("请输入新负责人（直接回车保持不变）：");
            
            if (!dname.isEmpty()) {
                dept.setDname(dname);
            }
            
            if (!head.isEmpty()) {
                dept.setHead(head);
            }
            
            if (departmentDAO.update(dept)) {
                System.out.println("院系信息更新成功！");
            } else {
                System.out.println("院系信息更新失败！");
            }
        } catch (Exception e) {
            System.out.println("更新院系失败：" + e.getMessage());
        }
    }
    
    private void deleteDepartment() {
        System.out.println("\n删除院系：");
        String dno = getStringInput("请输入要删除的院系编号：");
        
        try {
            if (departmentDAO.delete(dno)) {
                System.out.println("院系删除成功！");
            } else {
                System.out.println("院系删除失败！");
            }
        } catch (Exception e) {
            System.out.println("删除院系失败：" + e.getMessage());
            System.out.println("可能是因为有学生关联到此院系。");
        }
    }
    
    private void queryDepartment() {
        System.out.println("\n查询院系：");
        String dno = getStringInput("请输入要查询的院系编号：");
        
        try {
            Department dept = departmentDAO.getById(dno);
            
            if (dept != null) {
                System.out.println("查询结果：" + dept);
            } else {
                System.out.println("未找到该院系！");
            }
        } catch (Exception e) {
            System.out.println("查询院系失败：" + e.getMessage());
        }
    }
    
    // 宿舍管理
    private void manageDorms() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n----- 宿舍管理 -----");
            System.out.println("1. 查看所有宿舍");
            System.out.println("2. 添加宿舍");
            System.out.println("3. 更新宿舍");
            System.out.println("4. 删除宿舍");
            System.out.println("5. 查询宿舍");
            System.out.println("0. 返回主菜单");
            
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    listAllDorms();
                    break;
                case 2:
                    addDorm();
                    break;
                case 3:
                    updateDorm();
                    break;
                case 4:
                    deleteDorm();
                    break;
                case 5:
                    queryDorm();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
    }
    
    private void listAllDorms() {
        try {
            List<Dorm> dormList = dormDAO.getAll();
            if (dormList != null && !dormList.isEmpty()) {
                System.out.println("\n宿舍列表：");
                for (Dorm dorm : dormList) {
                    System.out.println(dorm);
                }
            } else {
                System.out.println("暂无宿舍信息。");
            }
        } catch (Exception e) {
            System.out.println("查询所有宿舍失败：" + e.getMessage());
        }
    }
    
    private void addDorm() {
        System.out.println("\n添加新宿舍：");
        String dormno = getStringInput("请输入宿舍编号（5位字符）：");
        String tele = getStringInput("请输入宿舍电话：");
        
        Dorm dorm = new Dorm(dormno, tele);
        try {
            if (dormDAO.add(dorm)) {
                System.out.println("宿舍添加成功！");
            } else {
                System.out.println("宿舍添加失败！");
            }
        } catch (Exception e) {
            System.out.println("添加宿舍失败：" + e.getMessage());
        }
    }
    
    private void updateDorm() {
        System.out.println("\n更新宿舍信息：");
        String dormno = getStringInput("请输入要更新的宿舍编号：");
        
        try {
            Dorm dorm = dormDAO.getById(dormno);
            
            if (dorm == null) {
                System.out.println("未找到该宿舍！");
                return;
            }
            
            System.out.println("当前宿舍信息：" + dorm);
            String tele = getStringInput("请输入新电话（直接回车保持不变）：");
            
            if (!tele.isEmpty()) {
                dorm.setTele(tele);
            }
            
            if (dormDAO.update(dorm)) {
                System.out.println("宿舍信息更新成功！");
            } else {
                System.out.println("宿舍信息更新失败！");
            }
        } catch (Exception e) {
            System.out.println("更新宿舍失败：" + e.getMessage());
        }
    }
    
    private void deleteDorm() {
        System.out.println("\n删除宿舍：");
        String dormno = getStringInput("请输入要删除的宿舍编号：");
        
        try {
            if (dormDAO.delete(dormno)) {
                System.out.println("宿舍删除成功！");
            } else {
                System.out.println("宿舍删除失败！");
            }
        } catch (Exception e) {
            System.out.println("删除宿舍失败：" + e.getMessage());
            System.out.println("可能是因为有学生关联到此宿舍。");
        }
    }
    
    private void queryDorm() {
        System.out.println("\n查询宿舍：");
        String dormno = getStringInput("请输入要查询的宿舍编号：");
        
        try {
            Dorm dorm = dormDAO.getById(dormno);
            
            if (dorm != null) {
                System.out.println("查询结果：" + dorm);
            } else {
                System.out.println("未找到该宿舍！");
            }
        } catch (Exception e) {
            System.out.println("查询宿舍失败：" + e.getMessage());
        }
    }
    
    // 课程管理
    private void manageCourses() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n----- 课程管理 -----");
            System.out.println("1. 查看所有课程");
            System.out.println("2. 添加课程");
            System.out.println("3. 更新课程");
            System.out.println("4. 删除课程");
            System.out.println("5. 查询课程");
            System.out.println("0. 返回主菜单");
            
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    listAllCourses();
                    break;
                case 2:
                    addCourse();
                    break;
                case 3:
                    updateCourse();
                    break;
                case 4:
                    deleteCourse();
                    break;
                case 5:
                    queryCourse();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
    }
    
    private void listAllCourses() {
        try {
            List<Course> courseList = courseDAO.getAll();
            if (courseList != null && !courseList.isEmpty()) {
                System.out.println("\n课程列表：");
                for (Course course : courseList) {
                    System.out.println(course);
                }
            } else {
                System.out.println("暂无课程信息。");
            }
        } catch (Exception e) {
            System.out.println("查询所有课程失败：" + e.getMessage());
        }
    }
    
    private void addCourse() {
        System.out.println("\n添加新课程：");
        String cno = getStringInput("请输入课程编号（2位字符）：");
        String cname = getStringInput("请输入课程名称：");
        String cpno = getStringInput("请输入先修课程号（若无，请直接回车）：");
        int credit = getIntInput("请输入学分：");
        String teacher = getStringInput("请输入授课教师：");
        
        if (cpno.isEmpty()) {
            cpno = null;
        }
        
        Course course = new Course(cno, cname, cpno, credit, teacher);
        try {
            if (courseDAO.add(course)) {
                System.out.println("课程添加成功！");
            } else {
                System.out.println("课程添加失败！");
            }
        } catch (Exception e) {
            System.out.println("添加课程失败：" + e.getMessage());
        }
    }
    
    private void updateCourse() {
        System.out.println("\n更新课程信息：");
        String cno = getStringInput("请输入要更新的课程编号：");
        
        try {
            Course course = courseDAO.getById(cno);
            
            if (course == null) {
                System.out.println("未找到该课程！");
                return;
            }
            
            System.out.println("当前课程信息：" + course);
            String cname = getStringInput("请输入新课程名称（直接回车保持不变）：");
            String cpno = getStringInput("请输入新先修课程号（若无，请输入'null'；直接回车保持不变）：");
            String creditStr = getStringInput("请输入新学分（直接回车保持不变）：");
            String teacher = getStringInput("请输入新授课教师（直接回车保持不变）：");
            
            if (!cname.isEmpty()) {
                course.setCname(cname);
            }
            
            if (!cpno.isEmpty()) {
                if ("null".equalsIgnoreCase(cpno)) {
                    course.setCpno(null);
                } else {
                    course.setCpno(cpno);
                }
            }
            
            if (!creditStr.isEmpty()) {
                try {
                    int credit = Integer.parseInt(creditStr);
                    course.setCredit(credit);
                } catch (NumberFormatException e) {
                    System.out.println("学分必须是整数，保持原值不变。");
                }
            }
            
            if (!teacher.isEmpty()) {
                course.setTeacher(teacher);
            }
            
            if (courseDAO.update(course)) {
                System.out.println("课程信息更新成功！");
            } else {
                System.out.println("课程信息更新失败！");
            }
        } catch (Exception e) {
            System.out.println("更新课程失败：" + e.getMessage());
        }
    }
    
    private void deleteCourse() {
        System.out.println("\n删除课程：");
        String cno = getStringInput("请输入要删除的课程编号：");
        
        try {
            if (courseDAO.delete(cno)) {
                System.out.println("课程删除成功！");
            } else {
                System.out.println("课程删除失败！");
                System.out.println("可能是因为有成绩记录或其他课程依赖此课程。");
            }
        } catch (Exception e) {
            System.out.println("删除课程失败：" + e.getMessage());
            System.out.println("可能是因为有成绩记录或其他课程依赖此课程。");
        }
    }
    
    private void queryCourse() {
        System.out.println("\n查询课程：");
        String cno = getStringInput("请输入要查询的课程编号：");
        
        try {
            Course course = courseDAO.getById(cno);
            
            if (course != null) {
                System.out.println("查询结果：" + course);
            } else {
                System.out.println("未找到该课程！");
            }
        } catch (Exception e) {
            System.out.println("查询课程失败：" + e.getMessage());
        }
    }
    
    // 学生管理
    private void manageStudents() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n----- 学生管理 -----");
            System.out.println("1. 查看所有学生");
            System.out.println("2. 添加学生");
            System.out.println("3. 更新学生信息");
            System.out.println("4. 删除学生");
            System.out.println("5. 查询学生");
            System.out.println("6. 根据院系查询学生");
            System.out.println("7. 根据宿舍查询学生");
            System.out.println("0. 返回主菜单");
            
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    listAllStudents();
                    break;
                case 2:
                    addStudent();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    queryStudent();
                    break;
                case 6:
                    queryStudentsByDepartment();
                    break;
                case 7:
                    queryStudentsByDorm();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
    }
    
    private void listAllStudents() {
        try {
            List<Student> studentList = studentDAO.getAll();
            if (studentList != null && !studentList.isEmpty()) {
                System.out.println("\n学生列表：");
                for (Student student : studentList) {
                    System.out.println(student);
                }
            } else {
                System.out.println("暂无学生信息。");
            }
        } catch (Exception e) {
            System.out.println("查询所有学生失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addStudent() {
        System.out.println("\n添加新学生：");
        String sno = getStringInput("请输入学号（6位字符）：");
        String sname = getStringInput("请输入姓名：");
        String sex = getStringInput("请输入性别（男/女）：");
        int sage = getIntInput("请输入年龄：");
        String dno = getStringInput("请输入院系编号：");
        String dormno = getStringInput("请输入宿舍编号：");
        
        Student student = new Student(sno, sname, sex, sage, dno, dormno);
        try {
            if (studentDAO.add(student)) {
                System.out.println("学生添加成功！");
            } else {
                System.out.println("学生添加失败！");
            }
        } catch (Exception e) {
            System.out.println("添加学生失败：" + e.getMessage());
        }
    }
    
    private void updateStudent() {
        System.out.println("\n更新学生信息：");
        String sno = getStringInput("请输入要更新的学号：");
        
        try {
            Student student = studentDAO.getById(sno);
            
            if (student == null) {
                System.out.println("未找到该学生！");
                return;
            }
            
            System.out.println("当前学生信息：" + student);
            String sname = getStringInput("请输入新姓名（直接回车保持不变）：");
            String sex = getStringInput("请输入新性别（直接回车保持不变）：");
            String sageStr = getStringInput("请输入新年龄（直接回车保持不变）：");
            String dno = getStringInput("请输入新院系编号（直接回车保持不变）：");
            String dormno = getStringInput("请输入新宿舍编号（直接回车保持不变）：");
            
            if (!sname.isEmpty()) {
                student.setSname(sname);
            }
            
            if (!sex.isEmpty()) {
                student.setSex(sex);
            }
            
            if (!sageStr.isEmpty()) {
                try {
                    int sage = Integer.parseInt(sageStr);
                    student.setSage(sage);
                } catch (NumberFormatException e) {
                    System.out.println("年龄必须是整数，保持原值不变。");
                }
            }
            
            if (!dno.isEmpty()) {
                student.setDno(dno);
            }
            
            if (!dormno.isEmpty()) {
                student.setDormno(dormno);
            }
            
            if (studentDAO.update(student)) {
                System.out.println("学生信息更新成功！");
            } else {
                System.out.println("学生信息更新失败！");
            }
        } catch (Exception e) {
            System.out.println("更新学生失败：" + e.getMessage());
        }
    }
    
    private void deleteStudent() {
        System.out.println("\n删除学生：");
        String sno = getStringInput("请输入要删除的学号：");
        
        try {
            if (studentDAO.delete(sno)) {
                System.out.println("学生删除成功！相关成绩记录也已删除。");
            } else {
                System.out.println("学生删除失败！");
            }
        } catch (Exception e) {
            System.out.println("删除学生失败：" + e.getMessage());
        }
    }
    
    private void queryStudent() {
        System.out.println("\n查询学生：");
        String sno = getStringInput("请输入要查询的学号：");
        
        try {
            Student student = studentDAO.getById(sno);
            
            if (student != null) {
                System.out.println("查询结果：" + student);
            } else {
                System.out.println("未找到该学生！");
            }
        } catch (Exception e) {
            System.out.println("查询学生失败：" + e.getMessage());
        }
    }
    
    private void queryStudentsByDepartment() {
        System.out.println("\n根据院系查询学生：");
        String dno = getStringInput("请输入院系编号：");
        
        try {
            List<Student> studentList = studentDAO.getByDepartment(dno);
            
            if (studentList != null && !studentList.isEmpty()) {
                System.out.println("查询结果：");
                for (Student student : studentList) {
                    System.out.println(student);
                }
            } else {
                System.out.println("该院系下没有学生或院系不存在！");
            }
        } catch (Exception e) {
            System.out.println("查询院系学生失败：" + e.getMessage());
        }
    }
    
    private void queryStudentsByDorm() {
        System.out.println("\n根据宿舍查询学生：");
        String dormno = getStringInput("请输入宿舍编号：");
        
        try {
            List<Student> studentList = studentDAO.getByDorm(dormno);
            
            if (studentList != null && !studentList.isEmpty()) {
                System.out.println("查询结果：");
                for (Student student : studentList) {
                    System.out.println(student);
                }
            } else {
                System.out.println("该宿舍下没有学生或宿舍不存在！");
            }
        } catch (Exception e) {
            System.out.println("查询宿舍学生失败：" + e.getMessage());
        }
    }
    
    // 成绩管理
    private void manageGrades() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n----- 成绩管理 -----");
            System.out.println("1. 查看所有成绩");
            System.out.println("2. 添加成绩");
            System.out.println("3. 更新成绩");
            System.out.println("4. 删除成绩");
            System.out.println("5. 查询成绩");
            System.out.println("6. 查询学生成绩");
            System.out.println("7. 查询课程成绩");
            System.out.println("0. 返回主菜单");
            
            int choice = getIntInput("请选择操作：");
            
            switch (choice) {
                case 1:
                    listAllGrades();
                    break;
                case 2:
                    addGrade();
                    break;
                case 3:
                    updateGrade();
                    break;
                case 4:
                    deleteGrade();
                    break;
                case 5:
                    queryGrade();
                    break;
                case 6:
                    queryGradesByStudent();
                    break;
                case 7:
                    queryGradesByCourse();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("无效的选择，请重试！");
            }
        }
    }
    
    private void listAllGrades() {
        try {
            List<Grade> gradeList = gradeDAO.getAll();
            if (gradeList != null && !gradeList.isEmpty()) {
                System.out.println("\n成绩列表：");
                for (Grade grade : gradeList) {
                    System.out.println(grade);
                }
            } else {
                System.out.println("暂无成绩信息。");
            }
        } catch (Exception e) {
            System.out.println("查询所有成绩失败：" + e.getMessage());
        }
    }
    
    private void addGrade() {
        System.out.println("\n添加新成绩：");
        String sno = getStringInput("请输入学号：");
        String cno = getStringInput("请输入课程号：");
        int score = getIntInput("请输入成绩（0-100）：");
        
        Grade grade = new Grade(sno, cno, score);
        try {
            if (gradeDAO.add(grade)) {
                System.out.println("成绩添加成功！");
            } else {
                System.out.println("成绩添加失败！");
            }
        } catch (Exception e) {
            System.out.println("添加成绩失败：" + e.getMessage());
        }
    }
    
    private void updateGrade() {
        System.out.println("\n更新成绩：");
        String sno = getStringInput("请输入学号：");
        String cno = getStringInput("请输入课程号：");
        
        try {
            Grade grade = gradeDAO.getById(sno, cno);
            
            if (grade == null) {
                System.out.println("未找到该成绩记录！");
                return;
            }
            
            System.out.println("当前成绩信息：" + grade);
            int score = getIntInput("请输入新成绩（0-100）：");
            
            grade.setScore(score);
            if (gradeDAO.update(grade)) {
                System.out.println("成绩更新成功！");
            } else {
                System.out.println("成绩更新失败！");
            }
        } catch (Exception e) {
            System.out.println("更新成绩失败：" + e.getMessage());
        }
    }
    
    private void deleteGrade() {
        System.out.println("\n删除成绩：");
        String sno = getStringInput("请输入学号：");
        String cno = getStringInput("请输入课程号：");
        
        try {
            if (gradeDAO.delete(sno, cno)) {
                System.out.println("成绩删除成功！");
            } else {
                System.out.println("成绩删除失败！");
            }
        } catch (Exception e) {
            System.out.println("删除成绩失败：" + e.getMessage());
        }
    }
    
    private void queryGrade() {
        System.out.println("\n查询成绩：");
        String sno = getStringInput("请输入学号：");
        String cno = getStringInput("请输入课程号：");
        
        try {
            Grade grade = gradeDAO.getById(sno, cno);
            
            if (grade != null) {
                System.out.println("查询结果：" + grade);
            } else {
                System.out.println("未找到该成绩记录！");
            }
        } catch (Exception e) {
            System.out.println("查询成绩失败：" + e.getMessage());
        }
    }
    
    private void queryGradesByStudent() {
        System.out.println("\n查询学生成绩：");
        String sno = getStringInput("请输入学号：");
        
        try {
            List<Grade> gradeList = gradeDAO.getByStudent(sno);
            
            if (gradeList != null && !gradeList.isEmpty()) {
                System.out.println("查询结果：");
                for (Grade grade : gradeList) {
                    System.out.println(grade);
                }
            } else {
                System.out.println("该学生暂无成绩记录或学生不存在！");
            }
        } catch (Exception e) {
            System.out.println("查询学生成绩失败：" + e.getMessage());
        }
    }
    
    private void queryGradesByCourse() {
        System.out.println("\n查询课程成绩：");
        String cno = getStringInput("请输入课程号：");
        
        try {
            List<Grade> gradeList = gradeDAO.getByCourse(cno);
            
            if (gradeList != null && !gradeList.isEmpty()) {
                System.out.println("查询结果：");
                for (Grade grade : gradeList) {
                    System.out.println(grade);
                }
            } else {
                System.out.println("该课程暂无成绩记录或课程不存在！");
            }
        } catch (Exception e) {
            System.out.println("查询课程成绩失败：" + e.getMessage());
        }
    }
    
    // 辅助方法
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("输入错误！请输入一个整数。");
            return -1;
        }
    }
}