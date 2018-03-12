(function($) {
    var defaults = {
        vertical: false,
        width: 140,
        groupWidth: 160,
        zIndex: 9999,
        hideArrow: false,
        menuItemSelector: 'li',
        menuGroupSelector: 'ul',
        rootClass: 'ui-menu',
        menuItemClass: 'ui-menu-item',
        menuGroupClass: 'ui-menu-group',
        verticalClass: 'ui-menu-vertical',
        horizontalClass: 'ui-menu-horizontal',
        hasVerticalClass: 'ui-menu-has-vertical',
        hasHorizontalClass: 'ui-menu-has-horizontal',
        hoverClass: 'ui-menu-hover',
        showDuration: 0,
        hideDuration: 0,
        hideDelayDuration: 0
    };

    function suppermenu() {
        var option = (typeof(arguments[0]) != 'string') ? $.extend(defaults, arguments[0]) : $.extend(defaults, {});

        var topMenuGroupClass = (option.vertical) ? option.verticalClass : option.horizontalClass,
            $menu = $(this).addClass(option.rootClass + ' ' + option.menuGroupClass + ' ' + topMenuGroupClass),
            $menuItems = $menu.find(option.menuItemSelector).addClass(option.menuItemClass),
            $menuGroups = $menu.find(option.menuGroupSelector).addClass(option.menuGroupClass);
        $('ul.ui-menu > li').addClass('ui-menu-li');
        //$('ul.ui-menu > li:first').addClass('current');
        $('ul.ui-menu > li > a').addClass('ui-menu-link');
        $('ul.ui-menu > li > ul').addClass('ui-menu-sub-ul');
        $('ul.ui-menu > li > ul > li > ul').addClass('ui-menu-last-ul');

        $('.ui-menu-sub').show();
  
        $('ul.ui-menu > li').each(function(index, item) {
            $(item).data('ullist', $(item).find('.ui-menu-sub-ul').html());
        })
        

        $menuItems.each(function() {
            if ($(this).find('a').text() == 'line') {
                $(this).addClass('line');
            }

        });

        $menuGroups.parent().each(function(index) {
            var $parentMenuItem = $(this); // menu item that has menu group
            var displayDirection = ($parentMenuItem.parent().hasClass(option.horizontalClass)) ? 'bottom' : 'right';
            $parentMenuItem.addClass((displayDirection == 'bottom') ? option.hasVerticalClass : option.hasHorizontalClass);
            if (!option.hideArrow) {
                $parentMenuItem.append((displayDirection == 'bottom') ? '<i class="iconfont icon-caret-down"></i>' : '<i class="iconfont icon-caret-right"></i>');
            }
            var $menuGroup = $parentMenuItem.find(option.menuGroupSelector + ':first').addClass(option.verticalClass);

            $parentMenuItem.click(
                function(e) {
                    var offset = {
                        left: '',
                        top: ''
                    };
                    $parentMenuItem.siblings().find(option.menuGroupSelector + ':first').hide();
                    /*if (displayDirection == 'bottom') {
                        offset.left = 0;
                    } else {
                        offset.left = $(this).width() + 'px';
                        offset.top = '0px';
                    }*/
                    //$menuGroup.css(offset).show();
                    $menuGroup.show();

                }
            )
        });
        
        

     
        $('ul.ui-menu > li').click(function() {
            var that = $(this);
            
            if(that.hasClass('current')){
                return false;
            };

            if(that.data('ullist')) {
                that.find('.ui-menu-sub-ul').html('');
                that.find('.ui-menu-sub-ul').append(that.data('ullist'));
            }

            that.find('.ui-menu-item').each(function(index, item){
                var _link = $(item).attr('link');
                if(_link != undefined) {
                    var encodedUrl = $.base64.encode(_link);
                    $(item).attr('hash', '#' + encodedUrl);
                }
            });

            var _hash = window.location.hash;
            var menuli = that.find('.ui-menu-item');
            for(var i = 0; i < menuli.length; i++) {
                var lihash = $(menuli[i]).attr('hash');
                if (lihash == _hash) {
                    var menuliitem = $(menuli[i]);
                    menuliitem.addClass('current');
                    menuliitem.parentsUntil('ui-menu-li').addClass('current');
                }
                if($(menuli[i]).find('a').width() > 95) {
                    $(menuli[i]).find('a').width(95);
                }
            }

            var menuwarpWidth = $menu.parent().width();

            $.suppermenumore(this, menuwarpWidth, []);

            
            return false;
        });



        $('.ui-menu-sub-ul > li').live("mouseover", function(e) {
            var that = $(this);
            var ulleft = $(this).offset().left;
            that.find('ul.ui-menu-last-ul').css('left', ulleft);
            that.find('ul.ui-menu-last-ul').show();
        });
        $('.ui-menu-sub-ul > li').live("mouseout", function() {
             var that = $(this);
            that.find('ul.ui-menu-last-ul').hide();
        });

        $(".ui-menu-more").live("mouseover",function(){
            $('#moregroup').show();
        });
        $(".ui-menu-more").live("mouseout",function(){
          $('#moregroup').hide();
        });

        $menu.find('.ui-menu-item').each(function(index, item){
            var encodedUrl = $.base64.encode($(item).attr('link'));
            $(item).attr('hash', '#' + encodedUrl);
        })

        if (option.vertical) {
            $menu.width(option.width);
            $menu.find('li').width(option.width);
            $menu.find('a').width(option.width - 40);
        } else {
            $.each($menuItems.find('li'), function() {
                //$(this).width(option.width);
                //$(this).width(option.groupWidth);
                if (!($(this).hasClass('ui-menu-li')) && $.browser.msie) {
                    $(this).children('a').width(option.width - 40);
                }
            });
        }
        $menu.css('zIndex', option.zIndex);
        $menu.show();
        $.suppermenuCarousel($('ul.ui-menu > li'), $menu.parent().width() - 260);
        
        

        return this;
    }
    $.fn.extend({
        suppermenu: suppermenu
    });
    $.suppermenuCarousel = function(el, menuwidth) {
        var menuulWidth = 0,
        menulist = [],
        menuulwarpWidth = menuwidth;

        el.each(function(i, item){
            $(item).show();
            menuulWidth += $(item).outerWidth();
            if (menuulWidth > menuulwarpWidth) {
                menulist.push($(item));
                $(item).hide();
            } else if (menuulWidth < menuulwarpWidth) {
                $(item).show();
                menulist.length = 0;
            } 

        })
        
        var menulistL = menulist.length;

        var i = 0;
        if(menulistL > 0) {
            $('.ui-menu-carousel').show();
            $('.ui-menu-left').addClass('active');
            $('.ui-menu-left').click(function() {
                if (i == menulistL) {
                    return;
                }
                if (!$(this).hasClass('active')) {
                    return;
                }
                el.eq(i).hide();
                menulist[i].show();
                $('.ui-menu-right').addClass('active');
                i++;
                if (i == (menulistL)) {
                    $('.ui-menu-left').removeClass('active');
                }
            })
            $('.ui-menu-right').click(function() {
                if (i == 0) {
                    return;
                }
                if (!$(this).hasClass('active')) {
                    return;
                }
                el.eq(i-1).show();
                menulist[i-1].hide();
                $('.ui-menu-left').addClass('active');
                i--;
                if (i == 0) {
                    $('.ui-menu-right').removeClass('active');
                }
            })
        } else {
            //$('.ui-menu-carousel').hide();
            $('.ui-menu-left').removeClass('active');
            $('.ui-menu-right').removeClass('active');
        }
    }
    $.suppermenumore = function(menuli, menuwidth, rli) {
            var $menuli = $(menuli);

                $menuli.addClass('current');
                $menuli.siblings().removeClass('current');
                $menuli.find('.ui-menu-sub-ul').show();
                $menuli.siblings().find('.ui-menu-sub-ul').hide();
                var menuliWidth = 0, 
                cutli = '';
                menuwidth = menuwidth - 80; 
                rli.length = 0;

                $menuli.find('.ui-menu-sub-ul > li > a').each(function(i, item) {
                    if($(item).width() > 95) {
                        $(item).width(95);
                    }
                    menuliWidth += $(item).parent().outerWidth();
                    if (menuliWidth > menuwidth) {
                        if($(item).text() != '更多') {
                            //cutli += $(item).parent().prop('outerHTML');
                            rli.push($(item).parent().prop('outerHTML'));
                        }
                        $(item).parent().remove();
                    }  

                });
                if((menuliWidth > menuwidth) && $menuli.find('.ui-menu-sub-ul > li > a').last().text() != '更多') {
                    $menuli.find('.ui-menu-sub-ul').append('<li class="ui-menu-item ui-menu-has-horizontal ui-menu-more"><a href="javascript:void(0);">更多</a></li>');
                }
                $('#moregroup').remove();
                $menuli.find('.ui-menu-sub-ul > li').last().show();
                var moregroup = document.createElement('ul'); 
                $(moregroup).attr('id', 'moregroup');
                $(moregroup).attr('class', 'moregroup');
                for( var j = 0; j < rli.length; j++) {
                    $(moregroup).append(rli[j]);
                }
                if (menuliWidth > menuwidth) {
                    $menuli.find('.ui-menu-sub-ul > li').last().append(moregroup);
                }
                $(moregroup).hide();
      
    }
    //module.exports = menu;
})($ || jQuery);