package com.mason.libgui.utils.vcs;

public final class KeyCommandFactory{


    private KeyCommandFactory(){}


    public static KeyCommand generateCharTypeCommand(char c, int position){
        return new KeyCommand(){

            @Override
            public String update(String str){
                return typeChar(str, position, c);
            }

            @Override
            public String inverse(String str){
                return deleteChar(str, position);
            }

        };
    }

    public static KeyCommand generateDeleteCharCommand(char del, int position){
        return new KeyCommand(){

            @Override
            public String update(String str){
                return deleteChar(str, position);
            }

            @Override
            public String inverse(String str){
                return typeChar(str, position, del);
            }

        };
    }

    private static String typeChar(String str, int position, char c){
        return str.substring(0, position) + c + str.substring(position);
    }

    private static String deleteChar(String str, int position){
        return str.substring(0, position) + str.substring(position+1);
    }

}
