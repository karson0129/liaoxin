package com.hyphenate.liaoxin.common.net.request;

import java.io.Serializable;
import java.util.List;

public class SystemBankRequst {


    public List<SystemBank> data;

    public class SystemBank implements Serializable {
//        "affixId":"ef45ce8c-f555-4461-ba50-e8b830d7580d","name":"民生银行","systemBankId":"956e957e-6c1f-4b16-9525-89242502812c"

        public String affixId;
        public String name;
        public String systemBankId;
        public String group;

    }
}
