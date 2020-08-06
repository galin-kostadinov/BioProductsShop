$(function () {
    loadAllProducts();
});

function loadAllProducts() {
    $.ajax({
        type: 'GET',
        url: '/api/products',
        data: 'json',
        success: function (products) {
            $.each(products, function (i, product) {
                addProductDOM(product);
            })
            loader.hide();
        }
    })
}

function addProductDOM({id, name, made, imgUrl, price, promotionalPrice}) {
    $('#product-list')
        .append($('<output/>')
            .addClass('col col-xl-3 col-lg-4 col-md-6 col-sm-12 text-center pb-md-5 ml-md-0 mr-md-0')
            .append($('<div/>')
                .addClass('col col-md-12 text-center')
                .append(
                    $('<h4/>')
                        .addClass('text-center mt-5')
                        .append(
                            $('<span/>')
                                .addClass('badge badge-pill badge-secondary')
                                .text(name)
                        )
                )
                .append(
                    $('<h6/>')
                        .addClass('col col-md-12')
                        .text('Made: ' + made)
                )
                .append(
                    $('<img/>', {src: imgUrl})
                        .addClass('col col-md-12 mb-md-3 mt-md-3')
                        .width('min-vw-300')
                        .height('min-vh-300')
                )
            )
        );

    if (promotionalPrice === null) {
        $('#product-list output:last-child')
            .append($('<div/>')
                .addClass('col col-md-12 mb-md-2 h5')
                .text('Price: $' + price.toFixed(2))
            );
    } else {
        $('#product-list output:last-child')
            .append($('<div/>')
                .addClass('form-inline col col-md-12 mb-md-2')
                .append(
                    $('<div/>')
                        .addClass('col col-md-5 h5 text-right pr-0 mr-0')
                        .text('Price: ')
                )
                .append(
                    $('<div/>')
                        .addClass('col col-md-2 h7 discount-price pl-1 pr-1 mr-0 ml-0')
                        .text(price.toFixed(2)
                        )
                )
                .append($('<div/>')
                    .addClass('col col-md-5 h5 text-left pl-0 ml-0')
                    .text('$' + promotionalPrice.toFixed(2))
                )
            );

    }

    $('#product-list output:last-child')
        .append($('<div/>')
            .addClass('col col-md-12')
            .append($('<a/>', {href: '/products/details/' + id})
                .addClass('btn btn-secondary glow-button font-weight-bold text-white')
                .text('Details')
            )
        );
}



