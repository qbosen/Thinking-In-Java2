package enumerated.custom;

import net.mindview.util.Enums;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Iterator;

/**
 * @author qiubaisen
 * @date 2018-12-17
 */

class Mail {
    private static long instanceCounter = 0;
    GeneralDelivery generalDelivery;
    Scannability scannability;
    Readability readability;
    Address address;
    ReturnAddress returnAddress;
    ForwardAddress forwardAddress;
    private long id = instanceCounter++;

    public static Mail randomMail() {
        Mail m = new Mail();
        m.generalDelivery = Enums.random(GeneralDelivery.class);
        m.scannability = Enums.random(Scannability.class);
        m.address = Enums.random(Address.class);
        m.readability = Enums.random(Readability.class);
        m.forwardAddress = Enums.random(ForwardAddress.class);
        m.returnAddress = Enums.random(ReturnAddress.class);
        return m;
    }

    public static Iterable<Mail> generator(int count) {
        return new Iterable<Mail>() {
            int n = count;

            @Override
            public Iterator<Mail> iterator() {
                return new Iterator<Mail>() {
                    @Override
                    public boolean hasNext() {
                        return n > 0;
                    }

                    @Override
                    public Mail next() {
                        n--;
                        return randomMail();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }


    public String details() {
        return "Mail{" +
                "id=" + id +
                ", generalDelivery=" + generalDelivery +
                ", scannability=" + scannability +
                ", readability=" + readability +
                ", address=" + address +
                ", forwardAddress=" + forwardAddress +
                ", returnAddress=" + returnAddress +
                '}';
    }

    @Override
    public String toString() {
        return "Mail " + id;
    }

    enum GeneralDelivery {
        YES, NO1, NO2, NO3, NO4, NO5
    }

    enum Scannability {
        UNSCANNABLE, YES1, YES2, YES3
    }

    enum Readability {
        ILLEGIBLE, YES1, YES2, YES3, YES4
    }

    enum Address {
        INCORRECT, OK1, OK2, OK3, OK4, OK5, OK6
    }

    enum ReturnAddress {
        MISSING, OK1, OK2, OK3, OK4, OK5
    }

    enum ForwardAddress {
        MISSING, OK1, OK2, OK3, OK4, OK5
    }
}

class PostOffice {
    private static EnumMap<MailHandler, Handler> handlerChain = new EnumMap<>(MailHandler.class);

    static {
        // 默认方案，可以重新装配
        handlerChain.put(MailHandler.GENERAL_DELIVERY, mail -> {
            switch (mail.generalDelivery) {
                case YES:
                    System.out.println(mail + "\t general handled");
                    return true;
                default:
                    return false;
            }
        });
        handlerChain.put(MailHandler.MACHINE_SCAN, mail -> {
            switch (mail.scannability) {
                case UNSCANNABLE:
                    return false;
                default:
                    switch (mail.address) {
                        case INCORRECT:
                            return false;
                        default:
                            System.out.println(mail + "\t auto delivery");
                            return true;
                    }
            }
        });
        handlerChain.put(MailHandler.VISUAL_INSPECTION, mail -> {
            switch (mail.readability) {
                case ILLEGIBLE:
                    return false;
                default:
                    switch (mail.address) {
                        case INCORRECT:
                            return false;
                        default:
                            System.out.println(mail + "\t artificially delivery");
                            return true;
                    }
            }
        });
        handlerChain.put(MailHandler.FORWARD_MAIL, mail -> {
            switch (mail.forwardAddress) {
                case MISSING:
                    return false;
                default:
                    System.out.println(mail + "\t forward delivery");
                    return true;
            }
        });
        handlerChain.put(MailHandler.RETURN_TO_SENDER, mail -> {
            switch (mail.returnAddress) {
                case MISSING:
                    return false;
                default:
                    System.out.println(mail + "\t return to sender");
                    return true;
            }
        });


    }

    public static void handle(Mail mail) {
        for (Handler handler : handlerChain.values()) {
            if (handler.handle(mail)) {
                return;
            }
        }
        System.out.println("DEAD MAIL");
    }


    enum MailHandler {
        GENERAL_DELIVERY,
        MACHINE_SCAN,
        VISUAL_INSPECTION,
        FORWARD_MAIL,
        RETURN_TO_SENDER;

    }

    interface Handler {
        boolean handle(Mail mail);
    }
}

public class PostOfficeDemo {
    @Test
    public void test() {
        for (Mail mail : Mail.generator(20)) {
            System.out.println(mail.details());
            PostOffice.handle(mail);
            System.out.println("**********");
        }
    }

    @Test
    public void testDeadMail() {
        Mail mail = new Mail();
        mail.generalDelivery = Mail.GeneralDelivery.NO1;
        mail.scannability = Mail.Scannability.UNSCANNABLE;
        mail.readability = Mail.Readability.ILLEGIBLE;
        mail.address = Mail.Address.OK1;
        mail.forwardAddress = Mail.ForwardAddress.MISSING;
        mail.returnAddress = Mail.ReturnAddress.MISSING;

        PostOffice.handle(mail);
    }
}
