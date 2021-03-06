package ru.capralow.dt.unit.launcher.plugin.core.frameworks.gson;

import com.google.gson.annotations.SerializedName;

public class FrameworkMethod {
	@SerializedName("name-en")
	public String nameEn = ""; //$NON-NLS-1$

	@SerializedName("name-ru")
	public String nameRu = ""; //$NON-NLS-1$

	@SerializedName("params")
	public FrameworkMethodParameter[] params = {};

	@SerializedName("returnedValue")
	private String returnedValue = ""; //$NON-NLS-1$

	@SerializedName("returnedValues")
	private String[] returnedValues = {};

	public int getMinParams() {
		int minParams = params.length;

		for (FrameworkMethodParameter param : params)
			if (Boolean.TRUE.equals(param.isDefaultValue))
				minParams--;

		return minParams;
	}

	public String[] getReturnedValues() {
		if (returnedValue.isEmpty() && returnedValues != null) {
			return returnedValues;
		}

		return new String[] { returnedValue };
	}
}
