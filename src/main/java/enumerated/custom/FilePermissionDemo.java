package enumerated.custom;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static enumerated.custom.FilePermissionDemo.Permission.*;

/**
 * @author qiubaisen
 * @date 2018-12-15
 */
public class FilePermissionDemo {
    enum Permission {
        r, w, x
    }

    class PermissionGroup {
        private EnumSet<Permission> ownerUser;
        private EnumSet<Permission> ownerGroup;
        private EnumSet<Permission> others;

        public PermissionGroup() {
            // 默认权限: 644
            ownerUser = EnumSet.of(r, w);
            ownerGroup = EnumSet.of(r);
            others = EnumSet.of(r);
        }

        public void processCMD(String authCmd) {
            String regex = "([ugoa]?)([+-=])([rwx]*)";

            Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(authCmd);
            if (!matcher.matches()) {
                throw new RuntimeException("cmd format wrong: " + authCmd);
            }
            String target = matcher.group(1);
            String operator = matcher.group(2);
            String permissions = matcher.group(3);

            List<EnumSet<Permission>> targetGroup = new ArrayList<>();

            switch (target) {
                case "u":
                    targetGroup.add(ownerUser);
                    break;
                case "g":
                    targetGroup.add(ownerGroup);
                    break;
                case "o":
                    targetGroup.add(others);
                    break;

                case "a":
                default:
                    targetGroup.add(ownerUser);
                    targetGroup.add(ownerGroup);
                    targetGroup.add(others);
                    break;
            }

            changePermissions(targetGroup, operator, permissions);
        }

        public void processCMDWithDetail(String authCmd) {
            String from = this.toString();
            processCMD(authCmd);
            String to = this.toString();

            System.out.println(from + "\t>>\t" + to + "\t\t" + authCmd);
        }

        private void changePermissions(List<EnumSet<Permission>> targetGroup, String op, String permissionStr) {
            EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);
            for (char c : permissionStr.toLowerCase().toCharArray()) {
                permissions.add(Permission.valueOf(String.valueOf(c)));
            }

            for (EnumSet<Permission> permissionGroup : targetGroup) {
                switch (op) {
                    case "+":
                        permissionGroup.addAll(permissions);
                        break;
                    case "-":
                        permissionGroup.removeAll(permissions);
                        break;
                    case "=":
                        permissionGroup.clear();
                        permissionGroup.addAll(permissions);
                        break;

                    default:
                        break;
                }
            }
        }


        @Override
        public String toString() {

            return groupPermissionString(ownerUser) +
                    groupPermissionString(ownerGroup) +
                    groupPermissionString(others);

        }

        private String groupPermissionString(EnumSet<? extends Permission> permissionGroup) {
            // 缺省
            final char dft = '-';
            StringBuilder stringBuilder = new StringBuilder();
            for (Permission value : values()) {
                stringBuilder.append(permissionGroup.contains(value) ? value.name() : dft);
            }
            return stringBuilder.toString();
        }
    }


    @Test
    public void test() {
        PermissionGroup permissionGroup = new PermissionGroup();
        System.out.println("default: "+permissionGroup);

        List<String> cmds = Arrays.asList(
                "=",
                "u+r",
                "o+wr",
                "a-r",
                "g+x",
                "=x",
                "a+wrxrxwwx",
                "-wx",
                "u=rw"
        );

        cmds.forEach(permissionGroup::processCMDWithDetail);
    }
}
