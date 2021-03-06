package mdz.ccuhistorian.webapp

import org.junit.Test

import groovy.time.BaseDuration

class TextFormatTest {

	@Test	
	public void testFormatDate() {
		def d=Date.parse('yyyy-MM-dd HH:mm:ss', '2016-12-31 23:59:58')
		assert TextFormat.formatDate(d)=='31.12.2016 23:59:58'
	}
	
	@Test	
	public void testParseDate() {
		assert TextFormat.parseDate('')==null
		assert TextFormat.parseDate('31.12.2017 23:59:58')==Date.parse('yyyy-MM-dd HH:mm:ss', '2017-12-31 23:59:58')
		assert TextFormat.parseDate('20171231235957')==Date.parse('yyyy-MM-dd HH:mm:ss', '2017-12-31 23:59:57')
	}
	
	@Test	
	public void testParseRelativeDate() {
		def d
		d=TextFormat.parseDate(null, '')
		assert d==null

		def rd=new Date()
		d=TextFormat.parseDate(rd, '')
		assert d==rd
		// should create new date instance
		assert !(d.is(rd))
		
		d=TextFormat.parseDate(TextFormat.parseDate('1.1.2018'), '')
		assert TextFormat.formatDate(d)=='01.01.2018 00:00:00'
		
		d=TextFormat.parseDate(TextFormat.parseDate('1.2.2018'), '1Y')
		assert TextFormat.formatDate(d)=='01.02.2019 00:00:00'

		d=TextFormat.parseDate(TextFormat.parseDate('2.1.2018'), '   -1Y  ')
		assert TextFormat.formatDate(d)=='02.01.2017 00:00:00'

		d=TextFormat.parseDate(TextFormat.parseDate('1.2.2018 11:22:33'), '-1Y')
		assert TextFormat.formatDate(d)=='01.02.2017 11:22:33'
		
		d=TextFormat.parseDate(TextFormat.parseDate('1.2.2018 11:22:33'), '-1Y+3D -1h-13M 10m-33s +1W')
		assert TextFormat.formatDate(d)=='11.01.2016 10:32:00'

		d=TextFormat.parseDate(TextFormat.parseDate('1.1.2018'), '2017=Y2=M3=D12=h31=m12=s')
		assert TextFormat.formatDate(d)=='03.02.2017 12:31:12'

		d=TextFormat.parseDate(TextFormat.parseDate('1.1.2018'), '  2017=Y 2=M 3=D 12=h 31=m 12=s \t')
		assert TextFormat.formatDate(d)=='03.02.2017 12:31:12'
		
		d=TextFormat.parseDate(TextFormat.parseDate('3.1.2018 11:12:13'), 'z')
		assert TextFormat.formatDate(d)=='03.01.2018 00:00:00'
	}
	
	@Test	
	public void testParseRelativeWeekOfYear() {
		// set week no., keep day of week
		
		def d=TextFormat.parseDate(TextFormat.parseDate('13.12.2017'), '52=W')
		assert TextFormat.formatDate(d)=='27.12.2017 00:00:00'
		
		d=TextFormat.parseDate(TextFormat.parseDate('31.5.2018'), '1=W')
		assert TextFormat.formatDate(d)=='04.01.2018 00:00:00'
		
		d=TextFormat.parseDate(TextFormat.parseDate('21.1.2019'), '1=W')
		assert TextFormat.formatDate(d)=='31.12.2018 00:00:00'
	}

	@Test	
	public void testParseRelativeDayOfWeek() {
		// set day of week, keep week no.
		
		def d=TextFormat.parseDate(TextFormat.parseDate('18.12.2017'), '1=w')
		assert TextFormat.formatDate(d)=='18.12.2017 00:00:00'
		
		d=TextFormat.parseDate(TextFormat.parseDate('13.12.2017'), '7=w')
		assert TextFormat.formatDate(d)=='17.12.2017 00:00:00'
	}
	
	@Test	
	public void testFormatDuration() {
		assert TextFormat.formatDuration(0)=='0:00:00,000'
		assert TextFormat.formatDuration(12)=='0:00:00,012'
		assert TextFormat.formatDuration(1234567890)=='342:56:07,890'
	}
}
