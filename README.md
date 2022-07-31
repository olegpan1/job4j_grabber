job4j_grabber
Агрегатор вакансий.
Система запускается по расписанию - раз в минуту.  
Период запуска указывается в настройках - app.properties.
Сайты для поиска: career.habr.com
Доступ к интерфейсу будет через REST API.

Расширение.
1. В проект можно добавить новые сайты без изменения кода.
2. В проекте можно сделать параллельный парсинг сайтов.

[![Build Status](https://app.travis-ci.com/olegpan1/job4j_grabber.svg?branch=master)](https://app.travis-ci.com/olegpan1/job4j_grabber)
[![codecov](https://codecov.io/gh/olegpan1/job4j_grabber/branch/master/graph/badge.svg?token=6HKA6FM5AL)](https://codecov.io/gh/olegpan1/job4j_grabber)