function ShowWin(url, width, height) {
	var scroll = (screen.width > 600) ? "yes" : "no";
	var left = (self.screen.width >> 1) - (width >> 1);
	var top = (self.screen.height >> 1) - (height >> 1);
	var param = 'left='+left+',top='+top+',width='+width+',height='+height+',';
	window.open(url, '_blank', param+'menubar=no,scrollbars=' + scroll + ',status=no');
}