package club.plus1;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class Chain {

    private static ArrayList<Block> chain = new ArrayList<Block>();
    public int difficulty = 5;

    // Превращаем класс в одиночку(Singleton) с помощью On Demand Holder
    public static class ChainHolder {
        public static final Chain HOLDER_INSTANCE = new Chain();
    }
    public static Chain get() {
        return ChainHolder.HOLDER_INSTANCE;
    }

    // Добавляем блок в блокчейн и возвращаем хеш добавленного блока
    public String add(String text, String hash){
        chain.add(new Block(text, hash));
        System.out.println("Идёт поиск подходящего блока");
        return last().mineBlock(difficulty);
    }

    // Проверка блокчейна на валидность
    public Boolean isValid() {
        Block thisBlock;
        Block prevBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < chain.size(); i++){
            thisBlock = chain.get(i);
            prevBlock = chain.get(i - 1);
            // Проверка текущего блока
            if(!thisBlock.hash.equals(thisBlock.calculateHash())){
                System.out.println("Текущий хеш некорректен");
                return false;
            }
            // Проверка предыдущего блока
            if(!prevBlock.hash.equals(thisBlock.prevHash)){
                System.out.println("Предыдущий хеш некорректен");
                return false;
            }
            // Проверка корректности майнинга
            if(!thisBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("Корректный блок не найден");
                return false;
            }
        }
        return true;
    }

    // Получение послденего блока цепочки
    public Block last(){
        return chain.get(chain.size()-1);
    }

    // Получение блокчейна в виде текста в формате Json
    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(chain);
    }
}
