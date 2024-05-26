new DataTable('#example', {
    initComplete: function () {
        this.api().columns([0, 1, 2, 4]).every( function () {
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
            target: 6,
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