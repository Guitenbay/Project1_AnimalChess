import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Created by 22752 on 2016/10/21.
 */
public class AnimalChess {
    //综合
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("斗兽棋游戏\n\n");
        all:
        while (true) {
            System.out.print("指令介绍:\n" +
                    "\n" +
                    "1. 移动指令\n" +
                    "        移动指令由两个部分组成。\n" +
                    "        第一个部分是数字1-8,根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n" +
                    "        第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n" +
                    "        比如指令 \"1d\" 表示鼠向右走, \"4w\" 表示狗向左走\n" +
                    "\n" +
                    "2. 游戏指令\n" +
                    "        输入 restart 重新开始游戏\n" +
                    "        输入 help 查看帮助\n" +
                    "        输入 undo 悔棋\n" +
                    "        输入 redo 取消悔棋\n" +
                    "        输入 exit 退出游戏\n");
            char[][] chess = new char[7][10];
            char[][] animalLeft = new char[7][10];
            char[][] animalRight = new char[7][10];
            Scanner scanner1 = new Scanner(new File("tile.txt"));
            Scanner scanner2 = new Scanner(new File("animalLeft.txt"));
            Scanner scanner3 = new Scanner(new File("animalRight.txt"));
            String input1 = gameString(scanner1);
            String input2 = gameString(scanner2);
            String input3 = gameString(scanner3);

            //输出地图
            boolean players = true;
            boolean pri = true;
            ArrayList<String> array2 = new ArrayList<>();
            ArrayList<String> array3 = new ArrayList<>();
            boolean[] cpp = new boolean[2];
            int site = -1;
            array2.add(0, input2);
            array3.add(0, input3);       //拷贝初始地图
            first:
            for (int count = 0; count < Math.pow(2, 31); count++) {
                boolean decide = gameW_Or_L(input2, input3, input1);
                if (decide) {
                    pri = false;
                    cpp[0] = pri;
                    cpp[1] = players;
                }
                site++;
                chess = gameChar(chess, input1);                       //输
                animalLeft = gameChar(animalLeft, input2);             //字
                animalRight = gameChar(animalRight, input3);           //符
                if (pri) {
                    //地图输出
                    gamePrint(chess, animalLeft, animalRight);
                }
                if (players && !decide) {
                    System.out.print("请左方玩家输入：");
                    String input = scanner.nextLine();
                    System.out.println(input);
                    /* help */
                    if (Objects.equals(input, "help")) {
                        System.out.print("指令介绍:\n" +
                                "\n" +
                                "1. 移动指令\n" +
                                "        移动指令由两个部分组成。\n" +
                                "        第一个部分是数字1-8,根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n" +
                                "        第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n" +
                                "        比如指令 \"1d\" 表示鼠向右走, \"4w\" 表示狗向左走\n" +
                                "\n" +
                                "2. 游戏指令\n" +
                                "        输入 restart 重新开始游戏\n" +
                                "        输入 help 查看帮助\n" +
                                "        输入 undo 悔棋\n" +
                                "        输入 redo 取消悔棋\n" +
                                "        输入 exit 退出游戏\n");
                    } else if (Objects.equals(input, "restart")) {
                        continue all;
      /* undo */
                    } else if (Objects.equals(input, "undo")) {
                        if (count > 0) {
                            input2 = array2.get(count - 1);
                            input3 = array3.get(count - 1);
                            animalLeft = gameChar(animalLeft, input2);
                            animalRight = gameChar(animalRight, input3);
                            pri = true;
                            players = !players;
                            count -= 2;           //计数值退后一步
                            site -= 1;            // 位置值不变
                            cpp[0] = pri;
                            cpp[1] = players;
                        } else {
                            System.out.println("已到初始位置！");
                            pri = false;
                            site--;
                            count -= 1;                          // count = 0 时什么都没有输入
                            cpp[0] = pri;
                            cpp[1] = players;
                        }
                        continue first;
      /* redo */
                    } else if (Objects.equals(input, "redo")) {
                        if (site > count) {
                            input2 = array2.get(count + 1);
                            input3 = array3.get(count + 1);
                            animalLeft = gameChar(animalLeft, input2);
                            animalRight = gameChar(animalRight, input3);
                            pri = true;
                            players = !players;
                            site -= 1;          //计数值自动加一，位置值不变
                            cpp[0] = pri;
                            cpp[1] = players;
                        } else {
                            System.out.println("已回到原来最后一步！");
                            pri = false;
                            site--;
                            count -= 1;                          // count回归时什么也没有输入
                            cpp[0] = pri;
                            cpp[1] = players;
                        }
                        continue first;
      /*exit*/
                    } else if (Objects.equals(input, "exit")) {
                        //break all;
                        System.exit(0);
   /* 输入数字字母*/
                    } else {
                        if (input.length() == 2) {
                            char ani = input.charAt(0);
                            char dir = input.charAt(1);
                            char trap = '2';
                            char home = '3';
                            count = gameRule(cpp, chess, animalLeft, animalRight, ani, dir, count, pri, players, trap, home);
                            site = count;
                        } else if (input.length() != 2) {
                            System.out.println("不能识别指令\"" + input + "\",请重新输入");
                            site--;
                            count--;
                            pri = false;
                            cpp[0] = pri;
                            cpp[1] = players;
                            continue first;
                        }
                    }

                } else if (!players && !decide) {
                    System.out.print("请右方玩家输入：");
                    String input = scanner.nextLine();
                    System.out.println(input);
                    /* help */
                    if (Objects.equals(input, "help")) {
                        System.out.print("指令介绍:\n" +
                                "\n" +
                                "1. 移动指令\n" +
                                "        移动指令由两个部分组成。\n" +
                                "        第一个部分是数字1-8,根据战斗力分别对应鼠(1),猫(2),狼(3),狗(4),豹(5),虎(6),狮(7),象(8)\n" +
                                "        第二个部分是字母wasd中的一个,w对应上方向,a对应左方向,s对应下方向,d对应右方向\n" +
                                "        比如指令 \"1d\" 表示鼠向右走, \"4w\" 表示狗向左走\n" +
                                "\n" +
                                "2. 游戏指令\n" +
                                "        输入 restart 重新开始游戏\n" +
                                "        输入 help 查看帮助\n" +
                                "        输入 undo 悔棋\n" +
                                "        输入 redo 取消悔棋\n" +
                                "        输入 exit 退出游戏\n");
      /* restart */
                    } else if (Objects.equals(input, "restart")) {
                        continue all;
      /* undo */
                    } else if (Objects.equals(input, "undo")) {
                        if (count > 0) {
                            input2 = array2.get(count - 1);
                            input3 = array3.get(count - 1);
                            animalLeft = gameChar(animalLeft, input2);
                            animalRight = gameChar(animalRight, input3);
                            pri = true;
                            players = !players;
                            count -= 2;           //计数值退后一步
                            site -= 1;            // 位置值不变
                            cpp[0] = pri;
                            cpp[1] = players;
                        } else {
                            System.out.println("已到初始位置！");
                            pri = false;
                            site--;
                            count -= 1;                          // count = 0 时什么都没有输入
                            cpp[0] = pri;
                            cpp[1] = players;
                        }
                        continue first;
      /* redo */
                    } else if (Objects.equals(input, "redo")) {
                        if (site > count) {
                            input2 = array2.get(count + 1);
                            input3 = array3.get(count + 1);
                            animalLeft = gameChar(animalLeft, input2);
                            animalRight = gameChar(animalRight, input3);
                            pri = true;
                            players = !players;
                            site -= 1;          //计数值自动加一，位置值不变
                            cpp[0] = pri;
                            cpp[1] = players;
                        } else {
                            System.out.println("已回到原来最后一步！");
                            pri = false;
                            site--;
                            count -= 1;     // count回归时什么也没有输入
                            cpp[0] = pri;
                            cpp[1] = players;
                        }
                        continue first;
      /* exit */
                    } else if (Objects.equals(input, "exit")) {
                        //break all;
                        System.exit(0);
  /* 输入数字字母 */
                    } else {
                        if (input.length() == 2) {
                            char ani = input.charAt(0);
                            char dir = input.charAt(1);
                            char trap = '4';
                            char home = '5';
                            count = gameRule(cpp, chess, animalRight, animalLeft, ani, dir, count, pri, players, trap, home);
                            site = count;
                        } else if (input.length() != 2) {
                            System.out.println("不能识别指令\"" + input + "\",请重新输入");
                            site--;
                            count--;
                            pri = false;
                            cpp[0] = pri;
                            cpp[1] = players;
                            continue first;
                        }
                    }
                } else {
                    String input = scanner.next();
                    if (Objects.equals(input, "restart"))
                        continue all;
                }
                pri = cpp[0];
                players = cpp[1];
                input2 = gameArrayToS(animalLeft);
                input3 = gameArrayToS(animalRight);
                array2.add(count + 1, input2);
                array3.add(count + 1, input3);
            }
        }
    }

    //读取文件
    private static String gameString(Scanner input) {
        String str = "";
        for (int i = 0; i < 7; i++) {
            str += input.nextLine();
            str += '\n';
        }
        return str;
    }

    //字符串转数组
    private static char[][] gameChar(char[][] arr, String input) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                arr[i][j] = input.charAt(i * 10 + j);
            }
        }
        return arr;
    }

    //数组转字符串
    private static String gameArrayToS(char[][] arr) {
        String str = "";
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                str += arr[i][j];
            }
        }
        return str;
    }

    //地图输出
    private static void gamePrint(char[][] arr1, char[][] arr2, char[][] arr3) {
        final char MOUSE = '1';
        final char CAT = '2';
        final char WOLF = '3';
        final char DOG = '4';
        final char PENTHER = '5';
        final char TIGER = '6';
        final char LION = '7';
        final char ELEPHONE = '8';
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                char one = arr1[i][j];
                char two = arr2[i][j];
                char thr = arr3[i][j];
                if (two != '0') {
                    switch (two) {
                        case MOUSE:
                            System.out.print("1鼠 ");
                            break;
                        case CAT:
                            System.out.print("2猫 ");
                            break;
                        case WOLF:
                            System.out.print("3狼 ");
                            break;
                        case DOG:
                            System.out.print("4狗 ");
                            break;
                        case PENTHER:
                            System.out.print("5豹 ");
                            break;
                        case TIGER:
                            System.out.print("6虎 ");
                            break;
                        case LION:
                            System.out.print("7狮 ");
                            break;
                        case ELEPHONE:
                            System.out.print("8象 ");
                            break;
                        case '\n':
                            System.out.println();

                    }
                } else if (thr != '0') {
                    switch (thr) {
                        case MOUSE:
                            System.out.print(" 鼠1");
                            break;
                        case CAT:
                            System.out.print(" 猫2");
                            break;
                        case WOLF:
                            System.out.print(" 狼3");
                            break;
                        case DOG:
                            System.out.print(" 狗4");
                            break;
                        case PENTHER:
                            System.out.print(" 豹5");
                            break;
                        case TIGER:
                            System.out.print(" 虎6");
                            break;
                        case LION:
                            System.out.print(" 狮7");
                            break;
                        case ELEPHONE:
                            System.out.print(" 象8");
                            break;
                        case '\n':
                            System.out.println();
                    }
                } else {
                    switch (one) {
                        case '0':
                            System.out.print(" 　 ");
                            break;
                        case '1':
                            System.out.print(" 水 ");
                            break;
                        case '2':
                            System.out.print(" 陷 ");
                            break;
                        case '3':
                            System.out.print(" 家 ");
                            break;
                        case '4':
                            System.out.print(" 陷 ");
                            break;
                        case '5':
                            System.out.print(" 家 ");
                            break;
                        case '\n':
                            System.out.println();
                    }
                }
            }
        }
    }

    //游戏规则
    private static int gameRule(boolean[] cpp, char[][] arr1, char[][] arr2, char[][] arr3, char ani, char dir, int count, boolean pri, boolean players, char trap, char home) {
        if (ani == '1' || ani == '2' || ani == '3' || ani == '4' || ani == '5' || ani == '6' || ani == '7' || ani == '8') {
            int loopCount = 0;
            here:
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 10; j++) {
                    if (arr2[i][j] == ani) {
                        switch (dir) {
          /* a */
                            case 'a':
                                if (j - 1 < 0) {
                                    System.out.println("动物不能超出边界");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j - 1] != '1' && arr2[i][j - 1] != '0') {
                                    System.out.print("与己方动物冲突，请再次输入：\n");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j - 1] == home) {
                                    System.out.println("己方动物不能进家！");
                                    count--;
                                    pri = false;
                                } else if ((ani == '6' || ani == '7') && arr1[i][j - 1] == '1') {
                                    if (arr2[i][j] < arr3[i][j - 4]) {
                                        System.out.println("你吃不了它，快跑！");
                                        count--;
                                        pri = false;
                                    } else if (arr3[i][j - 1] == '0' && arr3[i][j - 2] == '0' && arr3[i][j - 3] == '0') {
                                        arr2[i][j - 4] = ani;
                                        arr2[i][j] = '0';
                                        arr3[i][j - 4] = '0';
                                        pri = true;
                                        players = !players;
                                    } else {
                                        System.out.println("有鼠阻挡，不能过河");
                                        count--;
                                        pri = false;
                                    }
                                } else if (ani != '1' && arr1[i][j - 1] == '1') {
                                    System.out.println("该动物不能进入水中！");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j - 1] == trap) {
                                    arr2[i][j - 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j - 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if ((arr3[i][j - 1] == '8' && ani == '1') && arr1[i][j] != '1') {
                                    arr2[i][j - 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j - 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (arr3[i][j - 1] == '1' && ani == '8') {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                } else if (Integer.parseInt(String.valueOf(arr3[i][j - 1])) <= Integer.parseInt(String.valueOf(ani))) {
                                    arr2[i][j - 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j - 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (Integer.parseInt(String.valueOf(arr3[i][j - 1])) > Integer.parseInt(String.valueOf(ani))) {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                }
                                break here;
          /* d */
                            case 'd':
                                if (j + 1 > 8) {
                                    System.out.println("动物不能超出边界");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j + 1] != '1' && arr2[i][j + 1] != '0') {
                                    System.out.print("与己方动物冲突，请再次输入：\n");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j + 1] == home) {
                                    System.out.println("己方动物不能进家！");
                                    count--;
                                    pri = false;
                                } else if ((ani == '6' || ani == '7') && arr1[i][j + 1] == '1') {
                                    if (arr2[i][j] < arr3[i][j + 4]) {
                                        System.out.println("你吃不了它，快跑！");
                                        count--;
                                        pri = false;
                                    } else if (arr3[i][j + 1] == '0' && arr3[i][j + 2] == '0' && arr3[i][j + 3] == '0') {
                                        arr2[i][j + 4] = ani;
                                        arr2[i][j] = '0';
                                        arr3[i][j + 4] = '0';
                                        pri = true;
                                        players = !players;
                                    } else {
                                        System.out.println("有鼠阻挡，不能过河");
                                        count--;
                                        pri = false;
                                    }
                                } else if (ani != '1' && arr1[i][j + 1] == '1') {
                                    System.out.println("该动物不能进入水中！");
                                    count--;
                                    pri = false;
                                } else if (arr1[i][j + 1] == trap) {
                                    arr2[i][j + 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j + 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if ((arr3[i][j + 1] == '8' && ani == '1') && arr1[i][j] != '1') {
                                    arr2[i][j + 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j + 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (arr3[i][j + 1] == '1' && ani == '8') {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                } else if (Integer.parseInt(String.valueOf(arr3[i][j + 1])) <= Integer.parseInt(String.valueOf(ani))) {
                                    arr2[i][j + 1] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i][j + 1] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (Integer.parseInt(String.valueOf(arr3[i][j + 1])) > Integer.parseInt(String.valueOf(ani))) {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                }
                                break here;
          /* w */
                            case 'w':
                                if (i - 1 < 0) {
                                    System.out.println("动物不能超出边界");
                                    count--;
                                    pri = false;
                                } else if (arr1[i - 1][j] != '1' && arr2[i - 1][j] != '0') {
                                    System.out.print("与己方动物冲突，请再次输入：\n");
                                    count--;
                                    pri = false;
                                } else if (arr1[i - 1][j] == home) {
                                    System.out.println("己方动物不能进家！");
                                    count--;
                                    pri = false;
                                } else if ((ani == '6' || ani == '7') && arr1[i - 1][j] == '1') {
                                    if (arr2[i][j] < arr3[i - 3][j]) {
                                        System.out.println("你吃不了它，快跑！");
                                        count--;
                                        pri = false;
                                    } else if (arr3[i - 1][j] == '0' && arr3[i - 2][j] == '0') {
                                        arr2[i - 3][j] = ani;
                                        arr2[i][j] = '0';
                                        arr3[i - 3][j] = '0';
                                        pri = true;
                                        players = !players;
                                    } else {
                                        System.out.println("有鼠阻挡，不能过河");
                                        count--;
                                        pri = false;
                                    }
                                } else if (ani != '1' && arr1[i - 1][j] == '1') {
                                    System.out.println("该动物不能进入水中！");
                                    count--;
                                    pri = false;
                                } else if (arr1[i - 1][j] == trap) {
                                    arr2[i - 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i - 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if ((arr3[i - 1][j] == '8' && ani == '1') && arr1[i][j] != '1') {
                                    arr2[i - 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i - 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (arr3[i - 1][j] == '1' && ani == '8') {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                } else if (Integer.parseInt(String.valueOf(arr3[i - 1][j])) <= Integer.parseInt(String.valueOf(ani))) {
                                    arr2[i - 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i - 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (Integer.parseInt(String.valueOf(arr3[i - 1][j])) > Integer.parseInt(String.valueOf(ani))) {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                }
                                break here;
          /* s */
                            case 's':
                                if (i + 1 > 6) {
                                    System.out.println("动物不能超出边界");
                                    count--;
                                    pri = false;
                                } else if (arr1[i + 1][j] != '1' && arr2[i + 1][j] != '0') {
                                    System.out.print("与己方动物冲突，请再次输入：\n");
                                    count--;
                                    pri = false;
                                } else if (arr1[i + 1][j] == home) {
                                    System.out.println("己方动物不能进家！");
                                    count--;
                                    pri = false;
                                } else if ((ani == '6' || ani == '7') && arr1[i + 1][j] == '1') {
                                    if (arr2[i][j] < arr3[i + 3][j]) {
                                        System.out.println("你吃不了它，快跑！");
                                        count--;
                                        pri = false;
                                    } else if (arr3[i + 1][j] == '0' && arr3[i + 2][j] == '0') {
                                        arr2[i + 3][j] = ani;
                                        arr2[i][j] = '0';
                                        arr3[i + 3][j] = '0';
                                        pri = true;
                                        players = !players;
                                    } else {
                                        System.out.println("有鼠阻挡，不能过河");
                                        count--;
                                        pri = false;
                                    }
                                } else if (ani != '1' && arr1[i + 1][j] == '1') {
                                    System.out.println("该动物不能进入水中！");
                                    count--;
                                    pri = false;
                                } else if (arr1[i + 1][j] == trap) {
                                    arr2[i + 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i + 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if ((arr3[i + 1][j] == '8' && ani == '1') && arr1[i][j] != '1') {
                                    arr2[i + 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i + 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (arr3[i + 1][j] == '1' && ani == '8') {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                } else if (Integer.parseInt(String.valueOf(arr3[i + 1][j])) <= Integer.parseInt(String.valueOf(ani))) {
                                    arr2[i + 1][j] = ani;
                                    arr2[i][j] = '0';
                                    arr3[i + 1][j] = '0';
                                    pri = true;
                                    players = !players;
                                } else if (Integer.parseInt(String.valueOf(arr3[i + 1][j])) > Integer.parseInt(String.valueOf(ani))) {
                                    System.out.println("你吃不了它，快跑！");
                                    count--;
                                    pri = false;
                                }
                                break here;
                            default:
                                System.out.printf("错误指令：不对应任何方向\n");
                                count--;
                                pri = false;
                        }
                    } else
                        loopCount += 1;
                }
            }
            if (loopCount > 69) {
                System.out.printf("错误指令：该动物已死\n");
                count--;
                pri = false;
            }
        } else {
            System.out.printf("错误指令：不对应任何动物\n");
            count--;
            pri = false;
        }
        cpp[0] = pri;
        cpp[1] = players;
        return count;
    }

    //胜负判断
    private static boolean gameW_Or_L(String a, String b, String c) {
        char trap = '2';
        char home = '3';
        if (a.charAt(38) != '0') {
            System.out.println("左方玩家胜利:攻占敌方兽穴!\n输入 \"restart\" 重新开始");
            return true;
        } else if (b.charAt(30) != '0') {
            System.out.println("右方玩家胜利:攻占敌方兽穴!\n输入 \"restart\" 重新开始");
            return true;
        } else if (Objects.equals(b, "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n")) {
            System.out.println("左方玩家胜利:吃光敌方动物!\n输入 \"restart\" 重新开始");
            return true;
        } else if (Objects.equals(a, "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n" + "000000000\n")) {
            System.out.println("右方玩家胜利:吃光敌方动物!\n输入 \"restart\" 重新开始");
            return true;
        } else if (!gameAround(a, b, c, trap, home)) {
            System.out.println("右方玩家胜利:敌方无子可动!\n输入 \"restart\" 重新开始");
            return true;
        } else if (!gameAround(b, a, c, trap = '4', home = '5')) {
            System.out.println("左方玩家胜利:敌方无子可动!\n输入 \"restart\" 重新开始");
            return true;
        } else
            return false;
    }

    //无子可动1，判断某棋子四周是否能动
    private static boolean gameAround(String a, String b, String c, char trap, char home) {
        char[][] a1 = new char[7][10];
        a1 = gameChar(a1, a);
        char[][] b1 = new char[7][10];
        b1 = gameChar(b1, b);
        char[][] c1 = new char[7][10];
        c1 = gameChar(c1, c);
        boolean result = true;
        boolean pri1 = true;
        boolean pri2 = true;
        boolean pri3 = true;
        boolean pri4 = true;
        char dir = '1';
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (a1[i][j] != '0') {
                    dir = 'a';
                    pri1 = gameNotMove(c1, a1, b1, a1[i][j], dir, trap, home, i, j);
                    dir = 'd';
                    pri2 = gameNotMove(c1, a1, b1, a1[i][j], dir, trap, home, i, j);
                    dir = 'w';
                    pri3 = gameNotMove(c1, a1, b1, a1[i][j], dir, trap, home, i, j);
                    dir = 's';
                    pri4 = gameNotMove(c1, a1, b1, a1[i][j], dir, trap, home, i, j);
                    if (!pri1 && !pri2 && !pri3 && !pri4) {
                        result = false;
                    } else
                        return true;
                }
            }
        }
        return result;
    }

    //无子可动2，判断某棋子某方向上是否能动
    private static boolean gameNotMove(char[][] arr1, char[][] arr2, char[][] arr3, char ani, char dir, char trap, char home, int i, int j) {
        boolean pri = true;
        switch (dir) {
          /* a */
            case 'a':
                if (j - 1 < 0) {
                    pri = false;
                } else if (arr1[i][j - 1] != '1' && arr2[i][j - 1] != '0') {
                    pri = false;
                } else if (arr1[i][j - 1] == home) {
                    pri = false;
                } else if ((ani == '6' || ani == '7') && arr1[i][j - 1] == '1') {
                    pri = arr2[i][j] >= arr3[i][j - 4] && arr3[i][j - 1] == '0' && arr3[i][j - 2] == '0' && arr3[i][j - 3] == '0';
                } else if (ani != '1' && arr1[i][j - 1] == '1') {
                    pri = false;
                } else if (arr1[i][j - 1] == trap) {
                    pri = true;
                } else if ((arr3[i][j - 1] == '8' && ani == '1') && arr1[i][j] != '1') {
                    pri = true;
                } else if (arr3[i][j - 1] == '1' && ani == '8') {
                    pri = false;
                } else if (Integer.parseInt(String.valueOf(arr3[i][j - 1])) <= Integer.parseInt(String.valueOf(ani))) {
                    pri = true;
                } else if (Integer.parseInt(String.valueOf(arr3[i][j - 1])) > Integer.parseInt(String.valueOf(ani))) {
                    pri = false;
                }
                break;
          /* d */
            case 'd':
                if (j + 1 > 8) {
                    pri = false;
                } else if (arr1[i][j + 1] != '1' && arr2[i][j + 1] != '0') {
                    pri = false;
                } else if (arr1[i][j + 1] == home) {
                    pri = false;
                } else if ((ani == '6' || ani == '7') && arr1[i][j + 1] == '1') {
                    pri = arr2[i][j] >= arr3[i][j + 4] && arr3[i][j + 1] == '0' && arr3[i][j + 2] == '0' && arr3[i][j + 3] == '0';
                } else if (ani != '1' && arr1[i][j + 1] == '1') {
                    pri = false;
                } else if (arr1[i][j + 1] == trap) {
                    pri = true;
                } else if ((arr3[i][j + 1] == '8' && ani == '1') && arr1[i][j] != '1') {
                    pri = true;
                } else if (arr3[i][j + 1] == '1' && ani == '8') {
                    pri = false;
                } else if (Integer.parseInt(String.valueOf(arr3[i][j + 1])) <= Integer.parseInt(String.valueOf(ani))) {
                    pri = true;
                } else if (Integer.parseInt(String.valueOf(arr3[i][j + 1])) > Integer.parseInt(String.valueOf(ani))) {
                    pri = false;
                }
                break;
          /* w */
            case 'w':
                if (i - 1 < 0) {
                    pri = false;
                } else if (arr1[i - 1][j] != '1' && arr2[i - 1][j] != '0') {
                    pri = false;
                } else if (arr1[i - 1][j] == home) {
                    pri = false;
                } else if ((ani == '6' || ani == '7') && arr1[i - 1][j] == '1') {
                    pri = arr2[i][j] >= arr3[i - 3][j] && arr3[i - 1][j] == '0' && arr3[i - 2][j] == '0';
                } else if (ani != '1' && arr1[i - 1][j] == '1') {
                    pri = false;
                } else if (arr1[i - 1][j] == trap) {
                    pri = true;
                } else if ((arr3[i - 1][j] == '8' && ani == '1') && arr1[i][j] != '1') {
                    pri = true;
                } else if (arr3[i - 1][j] == '1' && ani == '8') {
                    pri = false;
                } else if (Integer.parseInt(String.valueOf(arr3[i - 1][j])) <= Integer.parseInt(String.valueOf(ani))) {
                    pri = true;
                } else if (Integer.parseInt(String.valueOf(arr3[i - 1][j])) > Integer.parseInt(String.valueOf(ani))) {
                    pri = false;
                }
                break;
          /* s */
            case 's':
                if (i + 1 > 6) {
                    pri = false;
                } else if (arr1[i + 1][j] != '1' && arr2[i + 1][j] != '0') {
                    pri = false;
                } else if (arr1[i + 1][j] == home) {
                    pri = false;
                } else if ((ani == '6' || ani == '7') && arr1[i + 1][j] == '1') {
                    pri = arr2[i][j] >= arr3[i + 3][j] && arr3[i + 1][j] == '0' && arr3[i + 2][j] == '0';
                } else if (ani != '1' && arr1[i + 1][j] == '1') {
                    pri = false;
                } else if (arr1[i + 1][j] == trap) {
                    pri = true;
                } else if ((arr3[i + 1][j] == '8' && ani == '1') && arr1[i][j] != '1') {
                    pri = true;
                } else if (arr3[i + 1][j] == '1' && ani == '8') {
                    pri = false;
                } else if (Integer.parseInt(String.valueOf(arr3[i + 1][j])) <= Integer.parseInt(String.valueOf(ani))) {
                    pri = true;
                } else if (Integer.parseInt(String.valueOf(arr3[i + 1][j])) > Integer.parseInt(String.valueOf(ani))) {
                    pri = false;
                }
                break;
            default:
                pri = false;
        }
        return pri;
    }
}

