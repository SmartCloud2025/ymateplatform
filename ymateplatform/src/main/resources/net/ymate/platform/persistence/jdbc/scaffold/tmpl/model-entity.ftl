package ${packageName}.model;

import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.annotation.Property;
import net.ymate.platform.persistence.annotation.Id;
import net.ymate.platform.persistence.annotation.Entity;
<#if (!isUseBaseModel)>import net.ymate.platform.persistence.jdbc.support.BaseEntity;</#if>

/**
 * <p>
 * ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if>
 * </p>
 * <p>
 * Code Generator By yMatePlatform  ${lastUpdateTime?string("yyyy-MM-dd a HH:mm:ss")}
 * </p>
 * 
 * @author JdbcScaffold
 * @version 0.0.0
 */
@Entity(name = "${tableName}")
public class ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if> extends <#if (isUseBaseModel)>BaseModel<${primaryKeyType}><#else>BaseEntity<${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if>, ${primaryKeyType}></#if> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	<#list fieldList as field>
	<#if primaryKeyName = field.varName>@Id</#if>
	<#if (field.columnName!"undefined") != "undefined">@Property(name = "${field.columnName}"<#if (field.autoIncrement)>, isAutoIncrement=true</#if>)</#if>
	private ${field.varType} ${field.varName};
	</#list>

	/**
	 * 构造器
	 */
	public ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if>() {
	}

    <#if (notNullableFieldList?size > 0)>
    /**
     * 构造器
    <#list notNullableFieldList as field>
     *	@param ${field.varName}
    </#list>
     */
    public ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if>(<#list notNullableFieldList as field>${field.varType} ${field.varName}<#if field_has_next>, </#if></#list>) {
        <#list notNullableFieldList as field>
        this.${field.varName} = ${field.varName};
        </#list>
    }
    </#if>

	/**
	 * 构造器
	<#list fieldList as field>
	 *	@param ${field.varName}
	</#list>
	 */
	public ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if>(<#list fieldList as field>${field.varType} ${field.varName}<#if field_has_next>, </#if></#list>) {
		<#list fieldList as field>
		this.${field.varName} = ${field.varName};
		</#list>
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.base.IEntity#getId()
	 */
	public ${primaryKeyType} getId() {
		return ${primaryKeyName};
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.persistence.base.IEntity#setId(java.lang.Object)
	 */
	public void setId(${primaryKeyType} id) {
		this.${primaryKeyName} = id;
	}

	<#list fieldList as field>
	<#if field.varName != 'id'>
	/**
	 * @return the ${field.varName}
	 */
	public ${field.varType} get${field.varName?cap_first}() {
		return ${field.varName};
	}

	/**
	 * @param ${field.varName} the ${field.varName} to set
	 */
	public void set${field.varName?cap_first}(${field.varType} ${field.varName}) {
		this.${field.varName} = ${field.varName};
	}
	</#if>

	</#list>

	/**
	 * <p>
	 * FIELDS
	 * </p>
	 * <p>
	 * ${modelName?cap_first}<#if (isUseClassSuffix)>Model</#if> 字段常量表
	 * </p>
	 * 
	 * @author JdbcScaffold
	 * @version 0.0.0
	 */
	public class FIELDS {
	<#list allFieldList as field>
		public static final ${field.varType} ${field.varName} = "${field.columnName}";
	</#list>
	}

	public static final String TABLE_NAME = JDBC.TABLE_PREFIX + "${tableName}";

}
