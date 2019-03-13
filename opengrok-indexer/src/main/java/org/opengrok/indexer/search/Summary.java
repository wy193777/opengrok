/*
 * Copyright 2005 The Apache Software Foundation
 * Portions Copyright (c) 2017, Chris Fraire <cfraire@me.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengrok.indexer.search;

import java.util.ArrayList;
import java.util.List;
import org.opengrok.indexer.web.Util;

/** A document summary dynamically generated to match a query. */
public class Summary {

    protected static String htmlize(String q) {
        return Util.prehtmlize(q);
    }

    /** A fragment of text within a summary. */
    public static class Fragment {
        private final String text;
        private final boolean needHTML;

        /* Constructs a fragment for the given text. */
        public Fragment(String text) {
            this.text = text;
            needHTML = true;
        }

        public Fragment(String text, boolean htmlize) {
            this.text = text;
            needHTML = htmlize;
        }

        /* Returns the text of this fragment. */
        public String getText() { return text; }

        /* Returns true iff this fragment is to be highlighted. */
        public boolean isHighlight() { return false; }

        /* Returns true iff this fragment is an ellipsis. */
        public boolean isEllipsis() { return false; }

        /* Returns an HTML representation of this fragment. */
        public String toString() { return needHTML ? htmlize(text) : text; }
    }

    /** A highlighted fragment of text within a summary. */
    public static class Highlight extends Fragment {
        /* Constructs a highlighted fragment for the given text. */
        public Highlight(String text, boolean needHTML) { super(text, needHTML); }

        /* Returns true. */
        public boolean isHighlight() { return true; }

        /* Returns an HTML representation of this fragment. */
        public String toString() { return super.needHTML ? "<b>" + super.toString() + "</b>" : super.toString(); }
    }

    /** An ellipsis fragment within a summary. */
    public static class Ellipsis extends Fragment {
        /* Constructs an ellipsis fragment for the given text. */
        public Ellipsis() { super(" ... "); }

        /* Returns true. */
        public boolean isEllipsis() { return true; }

        /* Returns an HTML representation of this fragment. */
        public String toString() { return "<b> ... </b>"; }
    }

    private final List<Fragment> fragments = new ArrayList<Fragment>();

    private static final Fragment[] FRAGMENT_PROTO = new Fragment[0];

    /* Adds a fragment to a summary.*/
    public void add(Fragment fragment) { fragments.add(fragment); }

    /**
     * Returns an array of all of this summary's fragments.
     * @return fragment array
     */
    public Fragment[] getFragments() {
        return fragments.toArray(FRAGMENT_PROTO);
    }

    /**
     * Returns an HTML representation of this fragment.
     * @return string representation
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < fragments.size(); i++) {
            buffer.append(fragments.get(i));
        }
        return buffer.toString();
    }
}
