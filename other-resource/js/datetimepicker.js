$(function() {
    $('.datetimepicker-input').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 60,
        showDropdowns: true,
        minDate: moment(),
        maxYear: 2050,
        locale: {
            format: 'DD/MMM/YYYY HH:00'
        },
    });
    $('input[name="endDatetime"]')(
        {

        }
    )
});

$('input[name="datehourrange"]').daterangepicker({
    "timePicker": true,
    "timePicker24Hour": true,
    "timePickerIncrement": 60,
    "startDate": "2024/Apr/20",
    "endDate": "2024/Apr/21",
    locale: {
        format: 'YYYY/MMM/DD HH:mm'
    }
}, function(start, end, label) {
    console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
});

