new DataTable('#example', {
    initComplete: function () {
        this.api().columns([1]).every( function () {
                let column = this;

                // Create select element
                let select = document.createElement('select');
                // Style select
                select.style.width = '100%';
                select.classList.add('form-control');


                select.add(new Option('All', ''));
                column.footer().replaceChildren(select);

                // Apply listener for user change in value
                select.addEventListener('change', function () {
                    column
                        .search(select.value, {exact: true})
                        .draw();
                });

                // Add list of options
                column
                    .data()
                    .unique()
                    .sort()
                    .each(function (d, j) {
                        select.add(new Option(d));
                    });
            });
        this.api().columns([0]).every( function () {
                let column = this;
                let title = column.header().textContent;

                // Create input element
                let input = document.createElement('input');
                input.placeholder = title;
                // Style input
                input.classList.add('form-control');
                input.style.padding = '0';

                column.footer().replaceChildren(input);

                // Event listener for user input
                input.addEventListener('keyup', () => {
                    if (column.search() !== this.value) {
                        column.search(input.value).draw();
                    }
                });
            });
    },
    layout: {
        topStart: 'pageLength',
        topEnd: 'search',
        bottomStart: 'info',
        bottomEnd: 'paging'
    },
    columnDefs: [
        {
            target: 5,
            searchable: false,
            sortable: false,
            selectable: false,
        }
    ],
    lengthMenu: [
        [5, 10, 25, 50, -1],
        [5, 10, 25, 50, 'All']
    ],
});