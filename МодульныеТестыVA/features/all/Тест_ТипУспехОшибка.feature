# language: ru

@tree
@classname=ModuleExceptionPath

Функционал: МодульныеТестыVA.Тест_ТипУспехОшибка
	Как Разработчик
	Я Хочу чтобы возвращаемое значение метода совпадало с эталонным
	Чтобы я мог гарантировать работоспособность метода

@OnServer
Сценарий: ПроверитьЗначениеНаТипНаЛожь
	И я выполняю код встроенного языка на сервере
	| 'Тест_ТипУспехОшибка.ПроверитьЗначениеНаТипНаЛожь(Контекст());' |

Сценарий: ПроверитьЗначениеНаТипНаЛожь
	И я выполняю код встроенного языка
	| 'Тест_ТипУспехОшибка.ПроверитьЗначениеНаТипНаЛожь(Контекст());' |