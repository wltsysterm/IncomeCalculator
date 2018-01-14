package com.wlt.product.incomecalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private Button button;

    private EditText totalMoneyView;
    private EditText shopMoneyView;
    private TextView totalTaxView;
    private TextView myTaxView;
    private TextView shopTaxView;
    private TextView totalLeftMoneyView;
    private TextView shopLeftMoneyView;
    private TextView myLeftMoneyView;
    private TextView consoleInfoView;

    private double totalMoney;
    private double shopMoney;
    private double totalTax;
    private double myTax;
    private double shopTax;
    private double totalLeftMoney;
    private double shopLeftMoney;
    private double myLeftMoney;

    /**
     * 换行符
     */
    private static final String nextRow = "\n";
    private static final String pattern = "^(([1-9][0-9]+)|([0-9]))(\\.[0-9]+)?$";
    /**
     * 控制台信息
     */
    private String consoleInfo;
    private String globalInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        totalMoneyView = findViewById(R.id.totalMoney);
        shopMoneyView = findViewById(R.id.shopMoney);
        consoleInfoView = findViewById(R.id.consoleInfo);

//         AlertDialog.Builder alterDialog = new AlertDialog.Builder(MainActivity2.this);
//        alterDialog.setTitle("提示框");
//        alterDialog.setMessage("提示内容");
//        alterDialog.setCancelable(false);
//        alterDialog.setPositiveButton("好的", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity2.this, "好的", Toast.LENGTH_SHORT).show();
//            }
//        });
//        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity2.this, "取消", Toast.LENGTH_SHORT).show();
//            }
//        });
//        alterDialog.show();


        totalTaxView = findViewById(R.id.totalTax);
        myTaxView = findViewById(R.id.myTax);
        shopTaxView = findViewById(R.id.shopTax);
        totalLeftMoneyView = findViewById(R.id.totalLeftMoney);
        shopLeftMoneyView = findViewById(R.id.shopLeftMoney);
        myLeftMoneyView = findViewById(R.id.myLeftMoney);


        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button: {
                clearConsoleInfo();
                //逻辑处理
                addConsoleInfo("分析结果:");
                if (!(String.valueOf(totalMoneyView.getText()).matches(pattern))) {
                    addConsoleInfo("税前总工资输入格式有误，请输入正确的金额！");
                    return;
                }
                if (!(String.valueOf(shopMoneyView.getText()).matches(pattern))) {
                    addConsoleInfo("店铺奖金输入格式有误，请输入正确的金额！");
                    return;
                }

                totalMoney = Double.parseDouble(String.valueOf(totalMoneyView.getText()));
                shopMoney =  Double.parseDouble(String.valueOf(shopMoneyView.getText()));

                if (shopMoney > totalMoney) {
                    addConsoleInfo("店铺金额已超过税前总工资，请调整");
                    return;
                }

                if (totalMoney > 38500) {
                    addConsoleInfo("税前总工资超过38500，脱离分析范围，请调整");
                    return;
                }
                addConsoleInfo("数据验证通过");

                totalTax = getTotalTax(totalMoney);
                addConsoleInfo(globalInfo);
                myTax = getMyTax(totalMoney, shopMoney);
                addConsoleInfo(globalInfo);
                shopTax = totalTax - myTax;
                globalInfo =  "店铺奖金扣税:"+totalTax+"-"+myTax+"="+shopTax;
                addConsoleInfo(globalInfo);
                totalLeftMoney = totalMoney - totalTax;
                globalInfo =  "税后总工资:"+totalMoney+"-"+totalTax+"="+totalLeftMoney;
                addConsoleInfo(globalInfo);
                shopLeftMoney = shopMoney - shopTax;
                globalInfo =  "税后个人工资:"+shopMoney+"-"+shopTax+"="+shopLeftMoney;
                addConsoleInfo(globalInfo);
                myLeftMoney = totalLeftMoney - shopLeftMoney;
                globalInfo =  "税后店铺奖金:"+totalLeftMoney+"-"+shopLeftMoney+"="+myLeftMoney;
                addConsoleInfo(globalInfo);
                addConsoleInfo("分析完毕");
                totalTaxView.setText(totalTax+"");
                myTaxView.setText(myTax+"");
                shopTaxView.setText(shopTax+"");
                totalLeftMoneyView.setText(totalLeftMoney+"");
                shopLeftMoneyView.setText(shopLeftMoney+"");
                myLeftMoneyView.setText(myLeftMoney+"");
            }
        }
    }

    public double getMyTax(double totalMoney, double shopMoney){
        double myMoney = totalMoney - shopMoney;
        //个人工资部分是否超过3500标识符
        boolean over3500 = (myMoney > 3500);
        if (!over3500) {
            globalInfo = "税前个人工资为："+myMoney+",未超过3500扣税临界线，个人扣税为0";
            return 0;
        }
        double result = 0;
        double border3500 = 3500;
        double border5000 = 5000;
        double border8000 = 8000;
        double border12500 = 12500;
        double border38500 = 38500;
        double over3500Money = totalMoney - shopMoney - 3500;
        if (border3500 < myMoney && myMoney <= border5000) {
            globalInfo = "税前个人工资在"+border3500+"和"+border5000+"之间，个人扣税：("+myMoney+"-"+border3500+")*0.03=";
            result = over3500Money * 0.03;
        } else if (border5000 < myMoney && myMoney  <= border8000) {
            globalInfo = "税前个人工资在"+border5000+"和"+border8000+"之间，个人扣税：("+myMoney+"-"+border3500+")*0.1-105=";
            result = over3500Money * 0.1 -  105;
        } else if (border8000 < myMoney && myMoney  <= border12500) {
            globalInfo = "税前个人工资在"+border8000+"和"+border12500+"之间，个人扣税：("+myMoney+"-"+border3500+")*0.2-555=";
            result = over3500Money * 0.2 -  555;
        } else if (border12500 < myMoney && myMoney  <= border38500) {
            globalInfo = "税前个人工资在"+border12500+"和"+border38500+"之间，个人扣税：("+myMoney+"-"+border3500+")* 0.25-1005=";
            result = over3500Money * 0.25 -  1005;
        }
        result = adaptNum(result);
        globalInfo+=result;
        return result;
    }
    public double getTotalTax(double totalMoney){
        //总工资是否超过3500标识符
        boolean over3500 = (totalMoney > 3500);
        if (!over3500) {
            globalInfo = "税前总工资未超过3500扣税临界线，总扣税为0";
            return 0;
        }
        double result = 0;
        double border3500 = 3500;
        double border5000 = 5000;
        double border8000 = 8000;
        double border12500 = 12500;
        double border38500 = 38500;
        double over3500Money = totalMoney - 3500;
        if (border3500 < totalMoney && totalMoney <= border5000) {
            globalInfo = "税前总工资在"+border3500+"和"+border5000+"之间，总扣税：("+totalMoney+"-"+border3500+")*0.03=";
            result = over3500Money * 0.03;
        } else if (border5000 < totalMoney && totalMoney  <= border8000) {
            globalInfo = "税前总工资在"+border5000+"和"+border8000+"之间，总扣税：("+totalMoney+"-"+border3500+")*0.1-105=";
            result = over3500Money * 0.1 -  105;
        } else if (border8000 < totalMoney && totalMoney  <= border12500) {
            globalInfo = "税前总工资在"+border8000+"和"+border12500+"之间，总扣税：("+totalMoney+"-"+border3500+")*0.2-555=";
            result = over3500Money * 0.2 -  555;
        } else if (border12500 < totalMoney && totalMoney  <= border38500) {
            globalInfo = "税前总工资在"+border12500+"和"+border38500+"之间，总扣税：("+totalMoney+"-"+border3500+")* 0.25-1005=";
            result = over3500Money * 0.25 -  1005;
        }
        result = adaptNum(result);
        globalInfo+=result;
        return result;
    }
    public double adaptNum(double num) {
        return Math.round(num * 1000) / 1000;
    }
        /**
         * 拼接所有的控制台信息
         * @param info
         */
    private void addConsoleInfo(String info){
        consoleInfo = String.valueOf(consoleInfoView.getText());
        consoleInfo += info;
        consoleInfo += nextRow;
        consoleInfoView.setText(consoleInfo);
    }

    /**
     * 清楚控制台信息
     */
    private void clearConsoleInfo(){
        consoleInfoView.setText("");
    }
}
