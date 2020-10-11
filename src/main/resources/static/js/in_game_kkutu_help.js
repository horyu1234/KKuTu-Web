import $ from 'jquery';

export default class Help {
    constructor() {
        this._bindEvents();
    }

    _bindEvents = () => {
        $("#list").children().each((menuIdx, menu) => {
            const $menu = $(menu);
            const menuId = "c" + (menuIdx + 1);
            this._initMenu($menu, menuId);

            const $subMenus = $menu.find("li");
            $subMenus.each((subMenuIdx, subMenu) => {
                const $subMenu = $(subMenu);
                const subMenuId = menuId + "p" + (subMenuIdx + 1);
                this._initMenu($subMenu, subMenuId);
            });
        });

        $("img").on('click', (e) => {
            const $target = $(e.currentTarget);
            this._showImagePopup($target.attr('src'));
        });
    }

    _showImagePopup = (url) => {
        window.open(url, "", [
            "resizable=no", "status=no"
        ].join(','));
    }

    _initMenu = ($menu, id) => {
        $menu.attr('id', id).on('click', this._clickMenuItem);
    }

    _clickMenuItem = (e) => {
        e.preventDefault();
        e.stopPropagation();

        $(".selected").removeClass("selected");
        const $target = $(e.currentTarget).addClass("selected");
        const id = $target.attr('id');
        const $tp = $target.parents("li");

        let title = $target.children("label").html();
        if ($tp.length) title = $tp.children("label").html() + " > " + title;
        $("#page-head").html(title);

        $(".page-body").hide();
        $("#box-" + id).show();
    }
}