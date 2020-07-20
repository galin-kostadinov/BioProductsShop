$(function () {
    loadAllPromotedProducts();
});

function loadAllPromotedProducts() {
    $.ajax({
        type: 'GET',
        url: '/api/products/promotions',
        data: 'json',
        success: function (products) {
            if (products.length > 0){
                addTitleDOM()
                $.each(products, function (i, product) {
                    addProductDOM(product);
                })
                loader.hide();
            }
        }
    })
}

function addTitleDOM() {
    $('#index-promoted-products')
        .append($('<h2/>')
            .addClass('text-center mt-5 mb-2 pb-0')
            .text('Our special offers:')
        )
        .append($('<block/>')
            .append($('<div/>')
                .addClass('row')
                .append($('<div/>')
                    .addClass('col col-md-12 text-center p-0')
                    .attr('id', 'index-promoted-products-row')
                )
            )
        );
}

function addProductDOM({id, name, made, imgUrl, price, promotionalPrice}) {
    $('#index-promoted-products')
        .append($('<output/>')
            .addClass('col col-md-3 col-sm-12 text-center pb-md-5 ml-md-0 mr-md-0')
            .append($('<div/>')
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
            )
            .append(
                $('<img/>', {src: imgUrl})
                    .addClass('col col-md-12 pt-1 pb-2')
                    .width('300px')
                    .height('300px')
            )
            .append($('<div/>')
                .addClass('form-inline col col-md-12 mb-md-2')
                .append($('<div/>')
                    .addClass('col col-md-5 h5 text-right pr-0 mr-0')
                    .text('Price:')
                )
                .append($('<div/>')
                    .addClass('col col-md-2 h7 discount-price pl-1 pr-1 mr-0 ml-0')
                    .text(price.toFixed(2))
                )
                .append($('<div/>')
                    .addClass('col col-md-5 h5 text-left pl-0 ml-0')
                    .text('$' + promotionalPrice.toFixed(2))
                )
            )
            .append($('<block/>')
                .addClass('col col-md-12')
                .append($('<a/>', {href: '/products/details/' + id})
                    .addClass('btn btn-secondary glow-button font-weight-bold text-white')
                    .text('Details')
                )
            )
        );
}

