/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.wicket.wickedcharts.fixture.dom;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.isisaddons.wicket.wickedcharts.fixture.dom.WickedChartsWicketToDoItem.Category;
import org.isisaddons.wicket.wickedcharts.fixture.dom.WickedChartsWicketToDoItem.Subcategory;
import org.joda.time.LocalDate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;

@DomainService(menuOrder = "10")
@Named("ToDos")
public class WickedChartsWicketToDoItems {

    public WickedChartsWicketToDoItems() {
    }
    
    // //////////////////////////////////////
    // Identification in the UI
    // //////////////////////////////////////

    public String getId() {
        return "toDoItems";
    }

    public String iconName() {
        return "ToDoItem";
    }

    // //////////////////////////////////////
    // NotYetComplete (action)
    // //////////////////////////////////////

    @Bookmarkable
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "1")
    public List<WickedChartsWicketToDoItem> notYetComplete() {
        final List<WickedChartsWicketToDoItem> items = notYetCompleteNoUi();
        if(items.isEmpty()) {
            container.informUser("All to-do items have been completed :-)");
        }
        return items;
    }

    @Programmatic
    public List<WickedChartsWicketToDoItem> notYetCompleteNoUi() {
        return container.allMatches(
                new QueryDefault<WickedChartsWicketToDoItem>(WickedChartsWicketToDoItem.class,
                        "todo_notYetComplete", 
                        "ownedBy", currentUserName()));
    }


    // //////////////////////////////////////
    // Complete (action)
    // //////////////////////////////////////
    
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "3")
    public List<WickedChartsWicketToDoItem> complete() {
        final List<WickedChartsWicketToDoItem> items = completeNoUi();
        if(items.isEmpty()) {
            container.informUser("No to-do items have yet been completed :-(");
        }
        return items;
    }

    @Programmatic
    public List<WickedChartsWicketToDoItem> completeNoUi() {
        return container.allMatches(
            new QueryDefault<WickedChartsWicketToDoItem>(WickedChartsWicketToDoItem.class,
                    "todo_complete", 
                    "ownedBy", currentUserName()));
    }


    // //////////////////////////////////////
    // NewToDo (action)
    // //////////////////////////////////////

    @MemberOrder(sequence = "40")
    public WickedChartsWicketToDoItem newToDo(
            final @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") @Named("Description") String description, 
            final @Named("Category") Category category,
            final @Named("Subcategory") Subcategory subcategory,
            final @Optional @Named("Due by") LocalDate dueBy,
            final @Optional @Named("Cost") BigDecimal cost, 
            final @Optional @Named("Previous cost") BigDecimal previousCost) {
        final String ownedBy = currentUserName();
        return newToDo(description, category, subcategory, ownedBy, dueBy, cost, previousCost);
    }
    public Category default1NewToDo() {
        return Category.Professional;
    }
    public Subcategory default2NewToDo() {
        return Category.Professional.subcategories().get(0);
    }
    public LocalDate default3NewToDo() {
        return clockService.now().plusDays(14);
    }
    public List<Subcategory> choices2NewToDo(
            final String description, final Category category) {
        return Subcategory.listFor(category);
    }
    public String validateNewToDo(
            final String description, 
            final Category category, 
            final Subcategory subcategory, 
            final LocalDate dueBy, 
            final BigDecimal cost,
            final BigDecimal previousCost) {
        return Subcategory.validate(category, subcategory);
    }

    // //////////////////////////////////////
    // AllToDos (action)
    // //////////////////////////////////////

    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "50")
    public List<WickedChartsWicketToDoItem> allToDos() {
        final String currentUser = currentUserName();
        final List<WickedChartsWicketToDoItem> items = container.allMatches(WickedChartsWicketToDoItem.class, WickedChartsWicketToDoItem.Predicates.thoseOwnedBy(currentUser));
        Collections.sort(items);
        if(items.isEmpty()) {
            container.warnUser("No to-do items found.");
        }
        return items;
    }

    // //////////////////////////////////////
    // AutoComplete
    // //////////////////////////////////////

    @Programmatic // not part of metamodel
    public List<WickedChartsWicketToDoItem> autoComplete(final String description) {
        return container.allMatches(
                new QueryDefault<WickedChartsWicketToDoItem>(WickedChartsWicketToDoItem.class,
                        "todo_autoComplete", 
                        "ownedBy", currentUserName(), 
                        "description", description));
    }


    // //////////////////////////////////////
    // Programmatic Helpers
    // //////////////////////////////////////

    @Programmatic // for use by fixtures
    public WickedChartsWicketToDoItem newToDo(
            final String description, 
            final Category category, 
            final Subcategory subcategory,
            final String userName, 
            final LocalDate dueBy, 
            final BigDecimal cost, BigDecimal previousCost) {
        final WickedChartsWicketToDoItem toDoItem = container.newTransientInstance(WickedChartsWicketToDoItem.class);
        toDoItem.setDescription(description);
        toDoItem.setCategory(category);
        toDoItem.setSubcategory(subcategory);
        toDoItem.setOwnedBy(userName);
        toDoItem.setDueBy(dueBy);
        toDoItem.setCost(cost);
        toDoItem.setPreviousCost(previousCost);

        container.persist(toDoItem);
        container.flush();

        return toDoItem;
    }
    
    private String currentUserName() {
        return container.getUser().getName();
    }

    
    // //////////////////////////////////////
    // Injected Services
    // //////////////////////////////////////

    @javax.inject.Inject
    private DomainObjectContainer container;

    @javax.inject.Inject
    private ClockService clockService;

}