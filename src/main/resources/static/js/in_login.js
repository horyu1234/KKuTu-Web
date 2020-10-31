import $ from 'jquery';

export default class Login {
    constructor() {
        $('.lbtn').on('click', (e) => {
            const $target = $(e.currentTarget);
            const vendorName = $target.attr('data-vendor-name');

            location.href = `/login/${vendorName}`;
        })
    }
}
