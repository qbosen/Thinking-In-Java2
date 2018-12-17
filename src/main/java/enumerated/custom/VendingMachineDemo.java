package enumerated.custom;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import static enumerated.custom.Input.*;
import static enumerated.custom.VendingMachine.State.RESTING;

/**
 * 所有输入对应的类别，对不同类别的输入进行不同处理
 */
enum Category {
    MONEY(JIAO_1, JIAO_5, YUAN_1, YUAN_5, YUAN_10),
    ITEM_SELECTION(WATER, INSTANT_NOODLES, COLA, SODA),
    QUIT_TRANSACTION(ABORT_TRANSACTION),
    SHUT_DOWN(STOP),
    START_SERVICE(START);

    private static EnumMap<Input, Category> referenceMap = new EnumMap<>(Input.class);

    static {
        for (Category category : Category.values())
            for (Input input : category.contents)
                referenceMap.put(input, category);
    }

    private Input[] contents;

    Category(Input... inputs) {
        this.contents = inputs;
    }

    public static Category getCategory(Input input) {
        return referenceMap.get(input);
    }
}

/**
 * 自动贩卖机所有操作输入
 * 投入金额，选择物品，退钱，关机
 */
enum Input {
    START {
        public int amount() { // Disallow
            throw new RuntimeException("SHUT_DOWN.amount()");
        }
    },
    // 按分计算金额
    JIAO_1(10), JIAO_5(50), YUAN_1(100), YUAN_5(500), YUAN_10(1000),
    WATER(200), INSTANT_NOODLES(450), COLA(250), SODA(400),
    ABORT_TRANSACTION {
        public int amount() { // Disallow
            throw new RuntimeException("ABORT.amount()");
        }
    },
    STOP {
        public int amount() { // Disallow
            throw new RuntimeException("SHUT_DOWN.amount()");
        }
    };
    private static Random rand = new Random(47);
    private int value; // In cents

    Input(int value) {
        this.value = value;
    }

    Input() {
    }


    public static Input randomSelection() {
        // Don't include STOP:
        return values()[rand.nextInt(values().length - 1)];
    }

    public int amount() {
        return value;
    }
}

/**
 * @author qiubaisen
 * @date 2018-12-17
 */
public class VendingMachineDemo {
/*    @Test
    public void test() {
        Input[] inputs = {YUAN_5, SODA, ABORT_TRANSACTION, STOP, START};
        new VendingMachine().run(inputs);
    }*/

    private static HashMap<String, Input> inputMap = new HashMap<>();

    static {
        addAlias(START, "start");
        addAlias(STOP, "stop");
        addAlias(ABORT_TRANSACTION, "a", "abort");
        addAlias(JIAO_1, "1j", "1jiao");
        addAlias(JIAO_5, "5j", "5jiao");
        addAlias(YUAN_1, "1y", "1yuan");
        addAlias(YUAN_5, "5y", "5yuan");
        addAlias(YUAN_10, "10y", "10yuan");
        addAlias(WATER, "w", "water");
        addAlias(INSTANT_NOODLES, "n", "noodle");
        addAlias(COLA, "c", "cola");
        addAlias(SODA, "s", "soda");

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VendingMachine machine = new VendingMachine();
        System.out.println("机器已启动...");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase();
            try {
                Input input = inputMap.get(line);
                machine.run(input);
            } catch (Exception e) {
                System.out.println("输入错误");
            }
        }
    }

    private static void addAlias(Input input, String... names) {
        for (String name : names) {
            inputMap.put(name, input);
        }
    }
}

class VendingMachine {

    private State state = RESTING;
    private int amount = 0;
    private Input selection = null;

    public void run(Input... inputs) {
        for (Input input : inputs) {
            state.toNext(input, this);
            state.print(this);
        }
    }


    enum State {
        //空闲
        RESTING {
            @Override
            void next(Input input, VendingMachine machine) {
                switch (Category.getCategory(input)) {
                    case MONEY:
                        machine.amount += input.amount();
                        machine.state = ADDING_MONEY;
                        break;
                    case SHUT_DOWN:
                        machine.state = TERMINAL;
                        break;
                }
            }

            @Override
            void print(VendingMachine machine) {
                System.out.println("运行中... 余额: " + machine.amount / 100d + " 元.");
            }
        },
        // 投币
        ADDING_MONEY {
            @Override
            void next(Input input, VendingMachine machine) {
                switch (Category.getCategory(input)) {
                    case MONEY:
                        machine.amount += input.amount();
                        break;
                    case ITEM_SELECTION:
                        machine.selection = input;
                        if (machine.amount < input.amount())
                            System.out.println("余额不足以购买: " + machine.selection + "\t价格: " + machine.selection.amount() / 100d +
                                    "元; \t 余额: " + machine.amount / 100d + " 元.");
                        else
                            machine.state = DISPENSING;
                        break;
                    case QUIT_TRANSACTION:
                        machine.state = GIVING_CHANGE;
                        break;
                    case SHUT_DOWN:
                        machine.state = TERMINAL;
                        break;
                }
            }

            @Override
            void print(VendingMachine machine) {
                System.out.println("投币中... 余额: " + machine.amount / 100d + " 元.");
            }
        },
        // 出货
        DISPENSING(StateDuration.TRANSIENT) {
            @Override
            void next(VendingMachine machine) {
                System.out.println("正在出货: " + machine.selection);
                machine.amount -= machine.selection.amount();
                machine.state = ADDING_MONEY;   // 继续购物
            }
        },
        // 退钱
        GIVING_CHANGE(StateDuration.TRANSIENT) {
            @Override
            void next(VendingMachine machine) {
                if (machine.amount > 0) {
                    System.out.println("退款: " + machine.amount / 100d + " 元.");
                    machine.amount = 0;
                }
                machine.state = RESTING;
            }
        },
        // 关机
        TERMINAL {
            @Override
            void next(Input input, VendingMachine machine) {
                switch (Category.getCategory(input)) {
                    case START_SERVICE:
                        machine.state = RESTING;
                        machine.amount = 0;
                        machine.selection = null;
                        System.out.println("开机 ~!");
                        break;
                    default:
                        System.out.println("<没有反应>...");
                }
            }

            @Override
            void print(VendingMachine machine) {
                System.out.println("关机 ~!");
            }
        },
        ;

        private boolean isTransient = false;

        State() {
        }

        State(StateDuration stateDuration) {
            // 临时状态,
            isTransient = true;
        }

        void next(Input input, VendingMachine machine) {
            throw new RuntimeException("Only Non-transient state has next(Input input, VendingMachine machine) method");
        }

        public void toNext(Input input, VendingMachine machine) {
            // 由外部操作进来 一定是非临时状态 所以下面的判断可有可无
            // 非临时状态执行后，可能为临时状态，等待机器回归非临时状态
            if (!machine.state.isTransient)
                machine.state.next(input, machine);
            while (machine.state.isTransient)
                machine.state.next(machine);
        }

        void next(VendingMachine machine) {
            throw new RuntimeException("Only call next() for StateDuration.TRANSIENT states");
        }

        void print(VendingMachine machine) {
            System.out.println("> " + machine.state);
        }

    }

    enum StateDuration {
        TRANSIENT,
    }

}