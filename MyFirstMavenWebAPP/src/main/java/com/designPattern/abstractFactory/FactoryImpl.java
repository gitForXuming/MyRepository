package com.designPattern.abstractFactory;

public class FactoryImpl implements Factory {
    @Override
    public Sender getSender(String type) {
        if ("phone".equals(type)) {
            return new PhoneSender();
        } else if ("mail".equals(type)) {
            return new MailSender();
        }
        return new MailSender();
    }

    public static void main(String[] args) {
        FactoryImpl factoryImpl = new FactoryImpl();
        Sender sender = factoryImpl.getSender("mail");
        sender.send("收到请回复我！");
    }

}
