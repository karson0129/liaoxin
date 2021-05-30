package com.hyphenate.liaoxin.common.net.request;

import java.util.List;

public class BankRequest {

    public List<BankItem> data;



    public class BankItem{

//        "clientBankId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//                "cardNumber": "string",
//                "realName": "string",
//                "systemBankId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//                "affixId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"

        public String clientBankId;
        public String cardNumber;
        public String realName;
        public String systemBankId;
        public String affixId;
        public String cardName;
        public String cardType;
        public String frontCardNumber;
        public String backCardNumber;

//        cardName  cardType frontCardNumber  backCardNumber

    }

}
