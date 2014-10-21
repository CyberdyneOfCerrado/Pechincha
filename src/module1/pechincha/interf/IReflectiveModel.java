package module1.pechincha.interf;

import module1.pechincha.util.DoAction;


public interface IReflectiveModel {
	public String getTableName();
    public String getColumnName();
    public String getColumnValues();
    public void biuldObject(DoAction doAction);
}
