/*
* Name: Joshua Acklin
* Class: CIT-244
* Date: 9 Oct 2017
* Description: Parts is comprised of objects used to track and monitor supply levels.
*/


abstract class Part{
	private String type;
	private String id;
	private String orderNumber;
	private double price;
        private boolean installed;
        private int cycles;

    public Part(String type, String id, double price, boolean installed, int cycles){
    	this.type = type;
    	this.id = id;
    	this.price = price;
        this.installed = installed;
        this.cycles = cycles;
    }
    
    public boolean getInstalled(){ return this.installed;}
    public void setInstalled(boolean b){this.installed = b;}
    public int getCycles(){ return this.cycles;}
    public void setCycles(int c){ this.cycles = c;}
    
    public String getType(){ return this.type;}
    public void setType(String t){ this.type = t;}
    public String getId(){ return this.id;}
    public void setId(String id){ this.id = id;}
    public String getOrderNumber(){ return this.orderNumber;}
    public void setOrderNumber(String o){ this.orderNumber = o;}
	public void setPrice(double p){this.price = p;}
	public double getPrice(){return this.price;}

    public String toString(){return "";}

}

class Body extends Part{
	private String name;
    public Body(String type, String id, double price, String name, boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setName(name);
    }
    public String getName(){return this.name;}
    public void setName(String n){this.name =n;}
    public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Model: " + this.getName() + " cycles: " + this.getCycles();
		}
}


class Exterior extends Part{
	private String color;
    public Exterior(String type, String id, double price, String color, boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
        setColor(color);
    }
    public String getColor(){return this.color;}
    public void setColor(String c){this.color = c;}
    public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Color: " + this.getColor();
		}
}

class Interior extends Part{
	private String color;
	private String material;

    public Interior(String type, String id, double price, String color, String material, boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
        setColor(color);
        setMaterial(material);
    }
    public String getColor(){return this.color;}
    public void setColor(String c){this.color = c;}
    public String getMaterial(){return this.material;}
    public void setMaterial(String m){this.material =m;}
    public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Color: " + this.getColor() + " Material: " + this.getMaterial();
		}
}


class Powertrain extends Part{
	private Boolean auto;
    public Powertrain(String type, String id, double price, Boolean auto,boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setAuto(auto);
    }
    public Boolean getAuto(){return this.auto;}
    public void setAuto(Boolean a){this.auto =a;}
		public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " AutoTrans: " + this.getAuto();
		}
}


class Seat extends Part{
	private String sType;
    public Seat(String type, String id, double price, String sType,boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setSType(sType);
    }
    public String getSType(){return this.sType;}
    public void setSType(String s){this.sType =s;}
		public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Seat Type: " + this.getSType();
		}
}


class Radio extends Part{
	private String console;

    public Radio(String type, String id, double price, String console,boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setConsole(console);
    }
    public String getConsole(){return this.console;}
    public void setConsole(String c){this.console =c;}
		public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Radio Type: " + this.getConsole();
		}
}


class Rim extends Part{
	private int size;
    public Rim(String type, String id, double price, int size, boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setSize(size);
    }
    public int getSize(){return this.size;}
    public void setSize(int s){this.size =s;}
		public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Rim Size: " + this.getSize();
		}
}


class Tire extends Part{
	private String tType;
    public Tire(String type, String id, double price, String tType,boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
			setTType(tType);
    }
    public String getTType(){return this.tType;}
    public void setTType(String t){this.tType =t;}
		public String toString(){return " Type: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
                        + " Tire Type: " + this.getTType();
		}
}


class Misc extends Part{
	private int index;
    public Misc(String type, String id, double price, int index, boolean installed, int cycles){
    	super(type, id, price, installed, cycles);
    	setIndex(index);
    }
    public int getIndex(){return this.index;}
    public void setIndex(int i){this.index =i;}
    public String toString(){return "";}
}


class DrivingAssist extends Misc{
	private String cType;
    public DrivingAssist(String type, String id, double price, int index, String cType, boolean installed, int cycles){
    	super(type, id, price, index, installed, cycles);
    	setCType(cType);
    }
    public String getCType(){return this.cType;}
    public void setCType(String c){this.cType =c;}
		public String toString(){return "\nType: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
      + " Index: " + this.getIndex() + " Driving Assist: " + this.getCType();
		}
}


class Roof extends Misc{
	private String rType;

    public Roof(String type, String id, double price, int index, String r,boolean installed, int cycles){
    	super(type, id, price, index, installed, cycles);
    	setRType(r);
    }
    public String getRType(){return rType;}
    public void setRType(String r){this.rType =r;}
    public String toString(){return "\nType: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
      + " Index: " + this.getIndex() + " Roof: " + this.getRType();
		}
}


class Backup extends Misc{
	private String bType;

    public Backup(String type, String id, double price, int index, String b, boolean installed, int cycles){
    	super(type, id, price, index, installed, cycles);
    	setBType(b);
    }
    public String getBType(){return this.bType;}
    public void setBType(String b){this.bType =b;}
    public String toString(){return "\nType: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
      + " Index: " + this.getIndex() + " Backup Sensor: " + this.getBType();
		}
}


class Sensor extends Misc{
	private String sType;
    public Sensor(String type, String id, double price, int index, String s,boolean installed, int cycles){
    	super(type, id, price, index, installed, cycles);
    	setSType(s);
    }
    public String getSType(){return this.sType;}
    public void setSType(String s){this.sType =s;}
    public String toString(){return "\nType: " + super.getType()
			+ " id: " + super.getId() + " Price: " + super.getPrice()
      + " Index: " + this.getIndex() + " Other Sensor: " + this.getSType();
		}
}

