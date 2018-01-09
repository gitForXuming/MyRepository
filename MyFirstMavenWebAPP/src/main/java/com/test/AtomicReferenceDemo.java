package com.test;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
	static class User{
        private String name;
        private int id;

        public User(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static AtomicReference<User> ar = new AtomicReference<User>();

    public static void main(String[] args){
        User user = new User("aa",11);
        ar.set(user);
        User newUser = new User("bb",22);
        ar.compareAndSet(user,newUser);
        System.out.println(ar.get().getName());
        System.out.println(ar.get().getId());
    }
}
