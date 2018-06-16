package tests;

import java.util.HashSet;
import java.util.Set;

import mathe.bündel.GruppenBündel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import boundingContext.gemeinsam.DoubleBündelMap;

@RunWith(SpringRunner.class)
public class BündelTest {

    @Test
    public void anlegen() {
        DoubleBündelMap<String> x = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Assert.assertEquals(2.0d, x.getBetrag("test2").doubleValue(), 0.0001d);
    }

    @Test
    public void keys() {
        DoubleBündelMap<String> x = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Set<String> s = new HashSet<>();
        s.add("test1");
        s.add("test2");
        s.add("test3");
        Assert.assertEquals(x.getKeys(), s);
    }

    @Test
    public void isunit() {
        DoubleBündelMap<String> x = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Assert.assertFalse(x.isUnit(x));
        x = new DoubleBündelFabrik().create("test", 0.0d, 0, 0d, 0, 0d);
        Assert.assertTrue(x.isUnit(x));
        x = new DoubleBündelFabrik().create("test");
        Assert.assertTrue(x.isUnit(x));
        Assert.assertTrue(x.isUnit(x.unit()));
    }

    @Test
    public void equals() {
        DoubleBündelMap<String> a = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        DoubleBündelMap<String> b = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Assert.assertTrue(a.equals(b));
        Assert.assertEquals(a.hashCode(), b.hashCode());
        b = new DoubleBündelFabrik().create("test", 2.0d, 2.0d, 3.0d);
        Assert.assertFalse(a.equals(b));
        b = new DoubleBündelFabrik().create("t", 1.0d, 2.0d, 3.0d);
        Assert.assertFalse(a.equals(b));
        b = new DoubleBündelFabrik().create("t", 0.0d, 0.0d, 0.0d);
        Assert.assertTrue(a.unit().equals(b));
        Assert.assertEquals(a.unit().hashCode(), b.hashCode());
    }

    @Test
    public void addAundB() {
        DoubleBündelMap<String> a = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        DoubleBündelMap<String> b = new DoubleBündelFabrik().create("test",
                3.0d, 2.0d, 1.0d);
        GruppenBündel<String, Double> c = a.add(a, b);
        Assert.assertEquals(4.0d, c.getBetrag("test1").doubleValue(), 0.0001d);
        Assert.assertEquals(4.0d, c.getBetrag("test2").doubleValue(), 0.0001d);
    }

    @Test
    public void substractAvonB() {
        DoubleBündelMap<String> a = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        DoubleBündelMap<String> b = new DoubleBündelFabrik().create("test",
                3.0d, 2.0d, 1.0d);
        GruppenBündel<String, Double> c = a.subtract(a, b);
        Assert.assertEquals(-2.0d, c.getBetrag("test1").doubleValue(), 0.0001d);
        Assert.assertEquals(0.0d, c.getBetrag("test2").doubleValue(), 0.0001d);
    }

    @Test
    public void summe() {
        DoubleBündelMap<String> a = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Assert.assertEquals(6.0d, a.getSumme().doubleValue(), 0.0001d);
    }

    @Test
    public void teilsumme() {
        DoubleBündelMap<String> a = new DoubleBündelFabrik().create("test",
                1.0d, 2.0d, 3.0d);
        Assert.assertEquals(5.0d, a.getSumme("test2", "test3").doubleValue(),
                0.0001d);
    }

}
