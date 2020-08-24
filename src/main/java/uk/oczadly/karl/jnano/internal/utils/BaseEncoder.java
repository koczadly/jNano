package uk.oczadly.karl.jnano.internal.utils;

/**
 * This implementation is based off of the JavaScript base32 modules created and modified by
 * <a href="https://github.com/LinusU">Linus Unnebäck (LinusU)</a> and <a href="https://github.com/termhn">Gray Olson
 * (termhn)</a>.
 */
public class BaseEncoder {
    
    private static final int TABLE_START = 32, TABLE_END = 128;
    
    
    private final char[] charLookupTable;
    private final byte[] charReverseTable = new byte[TABLE_END - TABLE_START];
    private Encoding base;
    
    
    public BaseEncoder(String alphabet) {
        this(alphabet.toCharArray());
    }
    
    public BaseEncoder(char[] alphabet) {
        this.base = Encoding.getFromBase(alphabet.length);
        this.charLookupTable = alphabet;
        
        //Initialize reverse table
        for(int i=0; i<charReverseTable.length; i++) charReverseTable[i] = -1; //Default values
        for(byte i=0; i<charLookupTable.length; i++) charReverseTable[(int)charLookupTable[i] - TABLE_START] = i; //Assign values
    }
    
    
    
    public boolean matchesCharacterSet(String str) {
        for(char c : str.toCharArray()) {
            try {
                this.getValueFromChar(c);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
    
    
    public char getCharFromValue(int value) {
        if(value < 0 || value >= charLookupTable.length) throw new IllegalArgumentException("Value " + value + " out of range");
        return charLookupTable[value];
    }
    
    public byte getValueFromChar(char c) {
        if((int)c < TABLE_START || (int)c >= TABLE_END) throw new IllegalArgumentException("Character '" + c + "' not part of set");
        byte val = charReverseTable[(int)c - TABLE_START];
        if(val == -1) throw new IllegalArgumentException("Character '" + c + "' not part of set");
        return val;
    }
    
    
    public String encode(byte[] rawBytes) {
        StringBuilder sb = new StringBuilder((int)(rawBytes.length * this.base.charsPerByte) + 1); //Approximate capacity
        
        int remainder = (rawBytes.length * 8) % this.base.bits;
        int offset = remainder == 0 ? 0 : this.base.bits - remainder;
        
        int value = 0;
        int remainingBits = 0; //Count of unprocessed bits
        
        for(byte byteRaw : rawBytes) {
            int byteInt = byteRaw & 0xFF; //Convert to unsigned
            value = (value << 8) | byteInt;
            remainingBits += 8;
            
            while(remainingBits >= this.base.bits) {
                int val = (value >>> (remainingBits + offset - this.base.bits)) & this.base.bitmask;
                sb.append(this.getCharFromValue(val));
                remainingBits -= this.base.bits;
            }
        }
        
        //Handle remaining bits
        if(remainingBits > 0) {
            int val = (value << (this.base.bits - (remainingBits + offset))) & this.base.bitmask;
            sb.append(this.getCharFromValue(val));
        }
        
        return sb.toString();
    }
    
    
    public byte[] decode(String data) {
        byte[] decoded = new byte[(int)Math.floor((data.length() * this.base.bits) / 8D)];
        
        int remainder = (data.length() * this.base.bits) % 8;
        int offset = remainder == 0 ? 0 : 8 - remainder;
        
        int value = 0;
        int index = 0;
        int remainingBits = 0; //Count of unprocessed bits
        boolean processedFirst = false;
        
        for(char c : data.toCharArray()) {
            value = (value << this.base.bits) | getValueFromChar(c);
            remainingBits += this.base.bits;
            
            if(remainingBits >= 8) {
                if(remainder != 0 && !processedFirst) { //Skip first element
                    processedFirst = true;
                } else {
                    decoded[index++] = (byte)((value >>> (remainingBits + offset - 8)) & 0xFF);
                }
                remainingBits -= 8;
            }
        }
        
        //Handle remaining bits
        if(remainingBits > 0) {
            decoded[index] = (byte)((value << (remainingBits + offset - 8)) & 0xFF);
        }
        return decoded;
    }
    
    
    
    private enum Encoding {
        BASE_1      (0),
        BASE_2      (1),
        BASE_4      (2),
        BASE_8      (3),
        BASE_16     (4),
        BASE_32     (5),
        BASE_64     (6),
        BASE_128    (7);
        
        
        private final int bits, bitmask, base;
        private final float charsPerByte;
        
        Encoding(int bits) {
            this.bits = bits;
            this.base = (int)Math.round(Math.pow(2, bits));
            this.bitmask = base - 1;
            this.charsPerByte = 8F / this.bits;
        }
        
        
        public static Encoding getFromBase(int base) {
            for(Encoding type : Encoding.values())
                if(type.base == base) return type;
            throw new IllegalArgumentException("Unknown numerical base");
        }
    }
    
}