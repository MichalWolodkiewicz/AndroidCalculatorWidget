package mikrosoft.widget.calc;

import android.test.AndroidTestCase;

import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class CalculatorDataTest extends AndroidTestCase {

    private static final String EXPRESSION = "100+1.56-0.456";

    CalculatorData calculatorData = new CalculatorData();

    public void testSaveAndReadExpression() {
        // given
        calculatorData.saveExpression(getContext(), EXPRESSION);

        //when
        String expression = calculatorData.readExpression(getContext());

        // then
        assertThat(expression, Matchers.equalTo(EXPRESSION));
    }

    public void testSaveFiveValues() {
        // given
        String[] fiveResults = new String[]{"12", "234", "4567", "654", "888665.5"};
        calculatorData.removeAllSavedResults(getContext());
        for (int i=fiveResults.length-1; i>=0 ; --i) {
            calculatorData.saveResult(getContext(), fiveResults[i]);
        }

        //when
        String[] savedResults = calculatorData.getSavedResults(getContext());

        // then
        assertThat(savedResults, Matchers.arrayWithSize(fiveResults.length));
        assertThat(savedResults, Matchers.arrayContaining(fiveResults));
    }

    public void testMaxResultsCase() {
        // given
        String[] tenResults = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] elevenResults = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        calculatorData.removeAllSavedResults(getContext());
        for (int i=elevenResults.length-1; i>=0 ; --i) {
            calculatorData.saveResult(getContext(), elevenResults[i]);
        }

        //when
        String[] savedResults = calculatorData.getSavedResults(getContext());

        // then
        assertThat(savedResults, Matchers.arrayWithSize(tenResults.length));
        assertThat(savedResults, Matchers.arrayContaining(tenResults));
    }

    public void testRemoveAllResults() {
        // given
        String[] fiveResults = new String[]{"1", "2", "3", "4", "5"};
        for (int i=fiveResults.length-1; i>=0 ; --i) {
            calculatorData.saveResult(getContext(), fiveResults[i]);
        }

        //when
        calculatorData.removeAllSavedResults(getContext());

        // then
        String[] savedResults = calculatorData.getSavedResults(getContext());
        assertThat(savedResults, Matchers.emptyArray());
    }
}