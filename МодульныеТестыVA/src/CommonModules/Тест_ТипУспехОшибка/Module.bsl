// @unit-test:failure
// Параметры:
// 	Фреймворк - ФреймворкТестирования - Фреймворк тестирования
Процедура ПроверитьЗначениеНаТипНаЛожь(Фреймворк) Экспорт
	ЭталонноеЗначение	= "Строка";
	ПроверяемыйТип		= Тип("Число");
	
	Фреймворк.ПроверитьТип(ЭталонноеЗначение, ПроверяемыйТип,
		"Типы ошибочно не совпадают.");
КонецПроцедуры
