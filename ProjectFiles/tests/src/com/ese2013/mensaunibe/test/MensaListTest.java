package com.ese2013.mensaunibe.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.google.android.gms.maps.model.LatLng;
import com.mensaunibe.app.model.Mensa;
import com.mensaunibe.app.model.MensaList;

public class MensaListTest extends TestCase {

	private Mensa mensa1, mensa2;
	private List<Mensa> mensas;
	private MensaList mensaList;

	public MensaListTest() {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		mensa1 = new Mensa(0, "Ein Name", "A Name", "Beschriebung",
				"Description", "Address", "City", 0, 0, null);
		mensa2 = new Mensa(1, "Ein Name2", "A Name2", "Beschriebung2",
				"Description2", "Address2", "City2", 1, 1, null);
		mensas = new ArrayList<Mensa>();
		mensas.add(mensa1);
		mensas.add(mensa2);
		mensaList = new MensaList(mensas);
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPreconditions() {
		assertNotNull(mensas);
		assertNotNull(mensaList);
	}
	
	public void testMensListShouldEqualItself() {
		assertEquals(mensaList, mensaList);
	}
	
	public void testEqualMensaListsShouldEqual() {
		MensaList other = new MensaList(mensas);
		assertEquals(mensaList, other);
	}

	public void testMensaListShouldReturnAllMensas() {
		assertEquals(mensaList.getMensas(), mensas);
	}

	public void testGettingMensaByExistingId() {
		assertEquals(mensaList.getMensaById(0), mensa1);
		assertEquals(mensaList.getMensaById(1), mensa2);
	}

	public void testGettingMensaByNonExistingId() {
		assertNull(mensaList.getMensaById(3));
	}

	public void testGettingMensaByCorrectLocation() {
		LatLng pos = new LatLng(0, 0);
		assertEquals(mensaList.getMensaByLocation(pos), mensa1);
		pos = new LatLng(1, 1);
		assertEquals(mensaList.getMensaByLocation(pos), mensa2);
	}

	public void testGettingMensaByCloseLocation() {
		LatLng pos = new LatLng(0.000001, 0);
		assertEquals(mensaList.getMensaByLocation(pos), mensa1);
		pos = new LatLng(1.000001, 1);
		assertEquals(mensaList.getMensaByLocation(pos), mensa2);
	}
	
	public void testGettingMensaByIncorrectLocation() {
		LatLng pos = new LatLng(10, 10);
		assertNull(mensaList.getMensaByLocation(pos));
	}

}
